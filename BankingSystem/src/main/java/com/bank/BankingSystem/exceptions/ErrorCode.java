package com.bank.BankingSystem.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USER_NOT_FOUND("404","User Not Found", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_FOUND("404","Account Not Found", HttpStatus.NOT_FOUND),
    WRONG_CREDENTIALS("401","Wrong Credentials", HttpStatus.UNAUTHORIZED),
    //ACCOUNT_ALREADY_IN_USE("409","Account Already In Use", HttpStatus.CONFLICT),
    ACCOUNT_ALREADY_EXISTS("409","Account Already Exists", HttpStatus.CONFLICT),
    MAXIMUM_USERACCOUNT_LIMIT_REACHED("409","Maximum UserAccount Limit Reached", HttpStatus.CONFLICT),

    AMOUNT_MUST_BE_POSITIVE("400","Amount must be positive", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE("500","Insufficient Balance", HttpStatus.INTERNAL_SERVER_ERROR),
    ANOTHER_THREAD_EXECTING("410","Another Thread Executing", HttpStatus.GONE),;

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
    public HttpStatus getStatus(){
        return this.status;
    }
}
