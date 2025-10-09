package com.bank.BankingSystem.dao;

import com.bank.BankingSystem.entities.User;

import java.util.Optional;

public interface UserDao {
    public Optional<User> findByUsername(String username);
    public User save(User user);
    public Optional<User> deleteUserByUsernameAndPassword(String username, String password);

}
