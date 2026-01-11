package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.model.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * REVIEW
     * 문제: 메소드 파라미터 명의 의미 불분명하여 가독성이 떨어짐
     * 원인: name 변수명 
     * 개선안: 해당 변수명을 category로 변경하여 의미 명확히 하는 것을 권장
     */
    Page<Product> findAllByCategory(String name, Pageable pageable);

    /**
     * REVIEW
     * 문제: 카테고리 조회 쿼리는 데이터 증가 시 중복 제거 비용이 커져 성능 저하가 발생할 수 있음
     * 원인: Product 전체 데이터를 DISTINCT 로 중복을 제거해 카테고리 목록을 조회
     * 개선안: category 칼럼의 index를 추가하여 중복 제거, 정렬 비용을 줄이거나
     *        Category 테이블을 별도로 분리하여 조회 비용을 줄이는 방법 고려
     *        정렬이 필요하면 jpql에 ORDER BY 명시
     */
    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
}