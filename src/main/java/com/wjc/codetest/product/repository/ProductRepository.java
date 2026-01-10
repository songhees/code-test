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
     * 문제: 가독성
     * 원인: name 변수명 
     * 개선안: 해당 변수명을 category로 변경하여 메서드의 목적과 일치시키는 것이 가독성 향상
     */
    Page<Product> findAllByCategory(String name, Pageable pageable);

    /**
     * REVIEW
     * 문제: DISTINCT 카테고리 조회 쿼리 설계
     * 원인: 카테고리 목록을 DISTINCT 로 조회
     * 개선안: 해당 쿼리는 full table scan과 중복 제거를 위한 정렬/해싱 비용 증가로 성능 저하 발생할 수 있음
     *      이를 해결하기 위해서는 category index를 추가하거나, Category 테이블을 별도로 분리하는 방법 고려
     */
    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
}