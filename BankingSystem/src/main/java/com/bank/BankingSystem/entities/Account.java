package com.bank.BankingSystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Random;

@Data
@Document(collection = "accounts")
public class Account {

    @Id
    private String accountNumber;

    @NotNull()
    private AccountType accountType;

    @NotNull()
    private Double balance;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @DBRef(lazy = true)  //TODO : indetail study for this
    @JsonBackReference
    private User user;


    public static String generateAccountNumber() {
        Random random = new Random();
        long number = 1000000000L + random.nextLong(9000000000L - 1000000000L);
        return String.valueOf(number);
    }


}
