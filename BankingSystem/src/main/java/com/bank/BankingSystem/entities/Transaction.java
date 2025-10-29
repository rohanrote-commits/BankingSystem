package com.bank.BankingSystem.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;


@Data
public class Transaction {
    @Id
    public String transactionId;

    public String accountNumber;
    public Double amount;
    public Operations operation;
   public LocalDateTime createdAt;
   public String transferdToAccountNumber;


}
