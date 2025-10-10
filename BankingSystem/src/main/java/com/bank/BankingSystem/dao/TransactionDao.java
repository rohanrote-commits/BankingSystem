package com.bank.BankingSystem.dao;

import com.bank.BankingSystem.entities.Transaction;

import java.util.List;

public interface TransactionDao {
    Transaction save(Transaction transaction);
    List<Transaction> finAllByAccountNumber(String accountNumber);
    void deleteAllByAccountNumber(String accountNumber);
}
