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
     * 원인: @ExceptionHandler(RuntimeException.class)에서 모든 런타임 예외를 잡고 모두 동일한 500 에러로 응답
     * 개선안: 세부적인 예외 상황에 대한 구분이 어려워짐 (예를들어 not found, invalid input 등)
     *      따라서 사용자 정의 예외 클래스를 만들어 유형별로 구체적인 예외 메세지와 HTTP 상태 코드를 전달하는 것이 좋음
     *      개발자의 예상치 못한 예외 발생시 응답을 위해 최상위 예외 핸들러(Execption)를 두는 것이 좋음
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
