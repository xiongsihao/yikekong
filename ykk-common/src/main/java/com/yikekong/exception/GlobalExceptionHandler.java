package com.yikekong.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BussinessException.class)
    public ResponseEntity<String> handleExcepting(BussinessException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExcepting(Exception e){
        log.error("error",e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器内部错误");
    }
}
