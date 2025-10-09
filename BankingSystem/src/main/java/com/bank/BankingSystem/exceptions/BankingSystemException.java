package com.bank.BankingSystem.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BankingSystemException extends RuntimeException{
   ErrorCode errorCode;

    public BankingSystemException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
