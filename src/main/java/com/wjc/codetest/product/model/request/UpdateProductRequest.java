package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * REVIEW
 * 문제: JSON 역직렬화 시 DTO 객체 생성 실패
 * 원인: UpdateProductRequest 클래스에 기본 생성자가 없고, 생성자 기반 역직렬화를 위한 @JsonCreator/@JsonProperty 지정이 없음
 * 개선안: 클래스에 @NoArgsConstructor 어노테이션을 추가하여 setter 방식으로 객체 생성하도록 변경 하거나
 *       @JsonCreator/@JsonProperty 어노테이션을 사용하여 특정 생성자를 지정하는 방법으로 해결
 */
@Getter
@Setter
public class UpdateProductRequest {
    private Long id;
    private String category;
    private String name;

    public UpdateProductRequest(Long id) {
        this.id = id;
    }

    public UpdateProductRequest(Long id, String category) {
        this.id = id;
        this.category = category;
    }

    public UpdateProductRequest(Long id, String category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }
}

