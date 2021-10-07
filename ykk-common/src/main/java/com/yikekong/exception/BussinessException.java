package com.yikekong.exception;

/**
 * 自定义逻辑异常
 */
public class BussinessException extends RuntimeException{
    private Integer errorCode;
    private String message;

    public BussinessException(Integer errorCode, String message) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public BussinessException(String message) {
        super(message);
        this.message = message;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
