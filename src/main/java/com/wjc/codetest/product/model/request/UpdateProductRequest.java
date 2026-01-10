package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * REVIEW
 * 문제: JSON 역직렬화 실패 오류
 * 원인: UpdateProductRequest 클래스에 여러 생성자 작성
 * 개선안: @RequestBody에서 Jackson이 JSON 값을 DTO로 역직렬화하여 객체로 변환할 때
 *        기본 생성자 + setter 메소드를 이용하여 생성할 수 없고
 *        생성자가 여러개인 상태에서 특정 생성자를 호출할 방법이 없기 때문에 오류 발생
 *        클래스에 @NoArgsConstructor 어노테이션을 추가하여 setter 방식으로 객체 생성하도록 변경
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

