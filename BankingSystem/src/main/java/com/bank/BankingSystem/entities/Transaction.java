package com.bank.BankingSystem.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "transactions")
public class Transaction {
     @Id
    public String transactionId;

    @DBRef(lazy = true)
    public Account account;
    public Double amount;
    public Operations operation;
   public LocalDateTime createdAt;
   public String transferdToAccountNumber;


}
