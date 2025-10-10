package com.bank.BankingSystem.dto;

import com.bank.BankingSystem.entities.Account;
import com.bank.BankingSystem.entities.Operations;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    public String transactionId;
    public String accountNumber;
    public Double amount;
    public Operations operation;
    public LocalDateTime createdAt;
    public String transferdToAccountNumber;
}
