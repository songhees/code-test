package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


/**
 * REVIEW
 * 문제: 도메인 무결성 취약
 * 원인: 엔티티 클래스에 @Setter 어노테이션 사용
 * 개선안: 모든 필드에 대해 무분별한 접근과 수정을 허용하기 때문에
 *        도메인 규칙이 깨지기 쉽고 예기치 않은 상태 변경이 발생할 수 있음
 *        필요한 필드에 대해서만 Setter 메서드를 직접 생성하여 흐름을 제어하는 것이 바람직
 * 
 * 문제: 중복된 코드
 * 원인: 엔티티 클래스의 @Getter 어노테이션과 별도로 getter 메서드 작성
 * 개선안: 별도의 getter 메서드 작성 필요없음
 */
@Entity
@Getter
@Setter
public class Product {
    /**
     * REVIEW
     * 문제: ID 생성 전략이 환경에 따라 달라 질 수 있어 동작이 명확하지 않음
     * 원인: strategy = GenerationType.AUTO 사용
     * 개선안: H2가 MODE=MySQL이지만 H2에서 시퀀스를 지원하기 때문에 AUTO가 SEQUENCE로 동작 가능하다.
     *        하지만 해당 앱의 목적 DB는 MySQL 이라면 GENERATIONTYPE.IDENTITY 사용으로 정확히 명시해주는 것이 좋음
     */
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * REVIEW
     * 문제: 가독성
     * 원인: category 와 name 필드의 @Column 어노테이션의 name 속성 생략
     * 개선안: 기본 매핑과 동일하면 생략 가능
     */    
    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    protected Product() {
    }

    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
