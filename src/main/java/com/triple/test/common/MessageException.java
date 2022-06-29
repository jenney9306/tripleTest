package com.triple.test.common;

public class MessageException extends RuntimeException{
    public static final String SUCCESS_CODE = "200";
    public static final String SUCCESS_MESSAGE = "SUCCESS";
    public static final String ERROR_CODE = "500";
    public static final String ERROR_MESSAGE = "SERVER_ERROR";
    public static final String VALIDATION_FAIL_CODE = "503";
    public static final String VALIDATION_FAIL_MESSAGE = "VALIDATION_ERROR";
}
