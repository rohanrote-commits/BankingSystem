//package com.bank.BankingSystem.repository;
//
//import com.bank.BankingSystem.entities.Transaction;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface TransactionRepo extends MongoRepository<Transaction, String> {
//
//    @Query(value = "{ 'account.accountNumber': ?0 }")
//    List<Transaction> findAllByAccountNumber(String accountNumber);
//
//
//}
