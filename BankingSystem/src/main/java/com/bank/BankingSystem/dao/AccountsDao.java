package com.bank.BankingSystem.dao;

import com.bank.BankingSystem.entities.Account;

import java.util.Optional;

public interface AccountsDao {
    boolean existsByAccountNumber(String accountNumber);
    long countByUserUsername(String username);
    boolean existsById(String candidate);
    Account save(Account account);
    Optional<Account> findByAccountNumber(String accountNumber);
    void deleteAccountByUsername(String username);

}
