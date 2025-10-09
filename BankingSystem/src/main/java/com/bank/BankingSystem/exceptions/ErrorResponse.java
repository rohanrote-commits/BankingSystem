package com.bank.BankingSystem.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    public String message;
    public String code;
    public HttpStatus status;

    public ErrorResponse(String message, String code,HttpStatus status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

}
