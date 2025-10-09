package com.bank.BankingSystem.dto;

import lombok.Data;
import org.bson.types.Decimal128;

@Data
public class DepositDto {
    public String accountNumber;
    public Double amount;

}
