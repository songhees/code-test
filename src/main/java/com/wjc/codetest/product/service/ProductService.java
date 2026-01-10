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
 * 문제: 트랜잭션 관리 설계 및 성능
 * 원인: 전반적인 Service 메소드의 트랜잭션 처리 미흡
 * 개선안: 트랜잭션이 필요한 메소드에 @Transactional 어노테이션 추가
 *      데이터의 일관성과 무결성을 보장하기 위해 트랜잭션 처리가 필요
 *      또한 JPA 사용 시 성능을 위해 Transactional(readOnly = true) 옵션도 고려
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

    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("product not found");
        }
        return productOptional.get();
    }

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

    public Page<Product> getListByCategory(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
    }

    /**
     * REVIEW
     * 문제: null 예외처리
     * 원인: 조회된 카테고리 목록이 null 일 경우 예외 처리 미흡
     * 개선안: 조회된 결과가 null 인지 확인하고 null 일 경우 빈 리스트 반환
     */
    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}