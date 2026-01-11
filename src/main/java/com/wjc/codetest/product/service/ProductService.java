package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * REVIEW
 * 문제: 트랜잭션 경계가 명확하지 않음
 * 원인: create/update/deleteById 등 변경 로직이 Service 메소드에서 @Transactional로 묶이지 않음
 * 개선안: 데이터 변경 메소드에 @Transactional 어노테이션을 추가하여 로직 흐름을 하나의 트랜잭션에 묶어
 *      원자성을 보장하도록 설계 필요
 *      또한 단순 조회 로직은 성능 최적화를 위해 Transactional(readOnly = true) 추가하는 옵션도 고려
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(CreateProductRequest dto) {
        Product product = new Product(dto.getCategory(), dto.getName());
        return productRepository.save(product);
    }

    /**
     * REVIEW
     * 문제: Optional 처리
     * 원인: Optional.isPresent()/Optional.get()
     * 개선안: Optional.orElseThrow() 메서드를 사용하여 코드 간결화 및 가독성 향상
     * 
     * 문제: 예외 처리 설계
     * 원인: throw new RuntimeException("product not found");
     * 개선안: RuntimeException 대신 커스텀 예외를 정의하고 GlobalExceptionHandler에서 404로 매핑되도록 설계하는 것이 좋음
     */
    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("product not found");
        }
        return productOptional.get();
    }

    /**
     * REVIEW
     * 문제: 업데이트 로직에서 save 호출이 불필요할 수 있음
     * 원인: productRepository.save(product);
     * 개선안: 트랜잭션내에서 조회한 엔티티는 영속성 컨텍스트에서 변경 감지 기능을 통해
     *      엔티티의 변경 사항을 자동으로 데이터베이스에 반영하므로 명시적인 save 호출을 생략하는 방안을 고려
     */
    public Product update(UpdateProductRequest dto) {
        Product product = getProductById(dto.getId());
        product.setCategory(dto.getCategory());
        product.setName(dto.getName());
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;
    }

    public void deleteById(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    /**
     * REVIEW
     * 문제: 정렬 기준이 의미가 없어 불필요한 정렬 비용 발생
     * 원인: 이미 category로 필터링 된 상태에서 Sort를 category로 설정
     * 개선안: 정렬 기준을 인덱스 기준 값으로 변경하여 조회 성능 향상 고려
     *      Spring Data page는 0 부터 시작하므로 클라이언트 단에서 page 를 1부터 시작한다면 dto.getPage() - 1 로 처리 필요
     */
    public Page<Product> getListByCategory(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
    }

    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}