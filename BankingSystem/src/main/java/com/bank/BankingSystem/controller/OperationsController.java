package com.bank.BankingSystem.controller;

import com.bank.BankingSystem.dto.DepositDto;
import com.bank.BankingSystem.dto.TransactionResponse;
import com.bank.BankingSystem.dto.TransferDto;
import com.bank.BankingSystem.entities.Account;
import com.bank.BankingSystem.entities.Transaction;
import com.bank.BankingSystem.service.OperationsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/banking/operations")
public class OperationsController {

    @Autowired
    private OperationsService operationsService;

    /**
     *
     *
     *
     *
     * @param account account is the entity that is being created
     * @return it returns the created user account
     * @author Rohan Rote
     * @description This method is used to create a new account for a user
     */
    //TODO : add try catch

    @PostMapping("/accounts") //TODO : meaningfull names shoulf
    public ResponseEntity<Account> createBankAccount(@RequestBody Account account, HttpServletRequest request) {
        String username = request.getAttribute("username").toString();
        operationsService.createBankAccount(account, username);
        return ResponseEntity.ok(account);
    }

    /**
     *
     * @param transaction is the object of DepositDto class that is passed ton
     * @return returns the transaction object
     */
    @PostMapping("/deposite")
    public ResponseEntity<Transaction> deposite(@RequestBody DepositDto transaction, HttpServletRequest request) {
        String username = request.getAttribute("username").toString();

        return ResponseEntity.ok(operationsService.deposit(transaction, username));
    }

    /**
     *
     * @param transaction DepositDto object that is passed in request body
     * @return returns the transaction object
     */
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody DepositDto transaction, HttpServletRequest request) {

      String username = request.getAttribute("username").toString();
        log.info("username " + request.getAttribute("username"));
        return ResponseEntity.ok(operationsService.withdraw(transaction, username));
    }

    /**
     *
     * @param transaction TransferDto object that is passed in request body
     * @return returns the transaction object
     */
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransferDto transaction, HttpServletRequest request) {
        String username = request.getAttribute("username").toString();
        return ResponseEntity.ok(operationsService.transfer(transaction, username));
    }

    /**
     *
     * @param accountNumber account number of the account is passed in request param
     * @return returns the account object
     */
    @GetMapping("/details")
    public ResponseEntity<Account> getAccountDetails(@RequestParam String accountNumber, HttpServletRequest request) {
        String username = request.getAttribute("username").toString();
        return ResponseEntity.ok(operationsService.getAccountDetails(accountNumber, username));
    }

    /**
     *
     * @param accountNumber account number of the account is passed in request param
     * @return returns the list of transaction objects
     */
    @GetMapping("/transaction")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@RequestParam String accountNumber, HttpServletRequest request) {
        String username = request.getAttribute("username").toString();
        return ResponseEntity.ok(operationsService.getTransactions(accountNumber, username));
    }

}
