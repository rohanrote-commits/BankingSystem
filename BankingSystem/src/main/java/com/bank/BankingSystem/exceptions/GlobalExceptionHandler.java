package com.bank.BankingSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BankingSystemException.class)
    public ResponseEntity<ErrorResponse> handleBankingSystemException(BankingSystemException exception){
        return new ResponseEntity<>(new ErrorResponse(exception.errorCode.getMessage(),exception.errorCode.getCode(),exception.errorCode.getStatus()),exception.errorCode.getStatus());
    }
}