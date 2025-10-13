package com.bank.BankingSystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;
import java.util.Random;

@Data
public class Account {

    @Id
    private String accountNumber;

    @NotNull()
    private AccountType accountType;

    @NotNull()
    private Double balance;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private String username;


    public static String generateAccountNumber() {
        Random random = new Random();
        long number = 1000000000L + random.nextLong(9000000000L - 1000000000L);
        return String.valueOf(number);
    }


}
