package com.bank.BankingSystem.dto;

import lombok.Data;

@Data
public class TransferDto {
    public String accountNumber;
    public Double amount;
    public String recipientAccountNumber;
}
