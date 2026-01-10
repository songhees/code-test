package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductListRequest {
    /**
     * REVIEW
     * 문제: 요청 파라미터 검증 설계
     * 원인: page, size, category 필드
     * 개선안: page, size, category 필수값에 대해 유효성 검증이 없음
     *        따라서 @NotNull, @NotBlank 등의 검증 어노테이션 추가
     *        그리고 null 또는 빈 값이 들어올 경우 기본값 설정 고려
     */
    private String category;
    private int page;
    private int size;
}