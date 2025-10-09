package com.bank.BankingSystem.repository;


import com.bank.BankingSystem.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //TODO :
public interface UserRepo extends MongoRepository<User, String> {

    Optional<User> deleteUserByUsernameAndPassword(String username, String password);
}
