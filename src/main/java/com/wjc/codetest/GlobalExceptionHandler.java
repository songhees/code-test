package com.wjc.codetest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
public class GlobalExceptionHandler {

    /**
     * REVIEW
     * 문제: ExceptionHandler 설계
     * 원인: 예외 처리를 할 예외 대상을 RuntimeException 으로 처리
     * 개선안: 모든 런타임 예외가 동일한 방식으로 처리됨
     *      세부적인 예외 상황에 대한 구분이 어려워짐
     *      따라서 사용자 정의 예외 클래스를 만들어 구체적인 예외 메세지를 전달하는 것이 좋음
     *      또한 개발자가 모든 예외를 파악하기 어려울 수 있기 때문에 기본 예외 메세지를 커스텀하는것도 고려
     */
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> runTimeException(Exception e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "runtimeException",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
