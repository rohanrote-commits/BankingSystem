package com.bank.BankingSystem.controller;

import com.bank.BankingSystem.dto.DepositDto;
import com.bank.BankingSystem.dto.TransactionResponse;
import com.bank.BankingSystem.dto.TransferDto;
import com.bank.BankingSystem.entities.Account;
import com.bank.BankingSystem.entities.Transaction;
import com.bank.BankingSystem.service.OperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
public class OperationsController {

    @Autowired
    private OperationsService operationsService;

    /**
     *
     *
     *
     *
     * @param account account is the entity that is being created
     * @param username username is the one who is creating the account
     * @param password password of the user
     * @return it returns the created user account
     * @author Rohan Rote
     * @description This method is used to create a new account for a user
     */
    //TODO : add try catch

    @PostMapping("/createBankAccount") //TODO : meaningfull names shoulf
    public ResponseEntity<Account> createBankAccount(@RequestBody Account account,
                                                     @RequestHeader("user-name") String username,
                                                     @RequestHeader("password") String password) {
        operationsService.createBankAccount(account, username, password);
        return ResponseEntity.ok(account);
    }

    /**
     *
     * @param transaction is the object of DepositDto class that is passed ton
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/deposite")
    public ResponseEntity<Transaction> deposite(@RequestBody DepositDto transaction, @RequestHeader("user-name") String username, @RequestHeader("password") String password) {

        return ResponseEntity.ok(operationsService.deposit(transaction, username, password));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody DepositDto transaction, @RequestHeader("user-name") String username, @RequestHeader("password") String password) {

        return ResponseEntity.ok(operationsService.withdraw(transaction, username, password));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransferDto transaction, @RequestHeader("user-name") String username, @RequestHeader("password") String password) {
        return ResponseEntity.ok(operationsService.transfer(transaction, username, password));
    }

    @GetMapping("/details")
    public ResponseEntity<Account> getAccountDetails(@RequestParam String accountNumber, @RequestHeader("user-name") String username, @RequestHeader("password") String password) {
        return ResponseEntity.ok(operationsService.getAccountDetails(accountNumber, username, password));
    }

    @GetMapping("/getTransactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@RequestParam String accountNumber, @RequestHeader("user-name") String username, @RequestHeader("password") String password) {
        return ResponseEntity.ok(operationsService.getTransactions(accountNumber, username, password));
    }

}
