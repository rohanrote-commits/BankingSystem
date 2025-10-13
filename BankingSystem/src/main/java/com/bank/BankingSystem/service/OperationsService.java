package com.bank.BankingSystem.service;

import com.bank.BankingSystem.dao.AccountDaoImpl;
import com.bank.BankingSystem.dao.TransactionDaoImpl;
import com.bank.BankingSystem.dao.UserDaoImpl;
import com.bank.BankingSystem.dto.DepositDto;
import com.bank.BankingSystem.dto.TransactionResponse;
import com.bank.BankingSystem.dto.TransferDto;
import com.bank.BankingSystem.entities.Account;
import com.bank.BankingSystem.entities.Operations;
import com.bank.BankingSystem.entities.Transaction;
import com.bank.BankingSystem.entities.User;
import com.bank.BankingSystem.exceptions.BankingSystemException;
import com.bank.BankingSystem.exceptions.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OperationsService {
    @Autowired
    private AccountDaoImpl accountDao;
    @Autowired
    private TransactionDaoImpl transactionDao;
    @Autowired
    private UserDaoImpl userDao;



    public Account createBankAccount(Account account,String username,String password){

        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new BankingSystemException(ErrorCode.USER_NOT_FOUND));

        if (!java.util.Objects.equals(user.getPassword(), password)) {
            throw new BankingSystemException(ErrorCode.WRONG_CREDENTIALS);
        }

        if(accountDao.countByUserUsername(username) > 2 ){
            throw new BankingSystemException(ErrorCode.MAXIMUM_USERACCOUNT_LIMIT_REACHED);
        }

//        account.setAccountNumber(Account.generateAccountNumber());
        while (true) {
            String candidate = Account.generateAccountNumber();
            if (!accountDao.existsById(candidate)) {
                account.setAccountNumber(candidate);
                break;
            }
        }

        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        account.setUsername(user.getUsername());
        user.setAccount(user.getAccounts());
        user.addAccount(account);
        userDao.save(user);
        return accountDao.save(account);
    }


    public Transaction deposit(DepositDto dto, String username, String password) {
        Optional<Account> account = accountDao.findByAccountNumber(dto.getAccountNumber());
        if (account.isEmpty()) {
            throw new BankingSystemException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        User u = userDao.findByUsername(username).orElse(null);
        if (u == null) {
            throw new BankingSystemException(ErrorCode.USER_NOT_FOUND);
        }
        if (!(Objects.equals(u.getUsername(), username) && Objects.equals(u.getPassword(), password))) {
            throw new BankingSystemException(ErrorCode.WRONG_CREDENTIALS);
        }
        Transaction transaction = new Transaction();
        transaction.setOperation(Operations.deposit);
        transaction.setAccount(account.get());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAmount(dto.getAmount());

        Double balance = account.get().getBalance() + dto.getAmount();
        account.get().setBalance(balance);
        accountDao.save(account.get());
        Transaction t = transactionDao.save(transaction);
        return transaction;
    }


    public Transaction withdraw(DepositDto dto, String username, String password) {
        Optional<Account> account = accountDao.findByAccountNumber(dto.getAccountNumber());
        if (account.isEmpty()) {
            throw new BankingSystemException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        User u = userDao.findByUsername(username).orElse(null);

        if (!(Objects.equals(u.getUsername(), username) && Objects.equals(u.getPassword(), password))) {
            throw new BankingSystemException(ErrorCode.WRONG_CREDENTIALS);
        }
        if (account.get().getBalance() < dto.getAmount()) {
            throw new BankingSystemException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        Transaction transaction = new Transaction();
        transaction.setOperation(Operations.withdrawal);
        transaction.setAccount(account.get());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAmount(dto.getAmount());

        Double balance = account.get().getBalance() - dto.getAmount();
        account.get().setBalance(balance);
        accountDao.save(account.get());
        Transaction t = transactionDao.save(transaction);
        return transaction;
    }


    public Transaction transfer(TransferDto dto, String username, String password) {
        Optional<Account> account = accountDao.findByAccountNumber(dto.getAccountNumber());
        if (account.isEmpty()) {
            throw new BankingSystemException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        User u = userDao.findByUsername(username).orElse(null);
        if (!(Objects.equals(u.getUsername(), username) && Objects.equals(u.getPassword(), password))) {
            throw new BankingSystemException(ErrorCode.WRONG_CREDENTIALS);
        }
        if (account.get().getBalance() < dto.getAmount()) {
            throw new BankingSystemException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        Optional<Account> recipientaccount = accountDao.findByAccountNumber(dto.getRecipientAccountNumber());
        if (account.isEmpty()) {
            throw new BankingSystemException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        Transaction transaction = new Transaction();
        transaction.setOperation(Operations.transfer);
        transaction.setAccount(account.get());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAmount(dto.getAmount());
        transaction.setTransferdToAccountNumber(dto.getRecipientAccountNumber());
        Double balance = account.get().getBalance() - dto.getAmount();
        account.get().setBalance(balance);
        recipientaccount.get().setBalance(recipientaccount.get().getBalance() + dto.getAmount());
        accountDao.save(account.get());
        accountDao.save(recipientaccount.get());
        Transaction t = transactionDao.save(transaction);
        return transaction;

    }

    public Account getAccountDetails(String accountNumber,String username,String password){
        Optional<Account> account = accountDao.findByAccountNumber(accountNumber);
        if (account.isEmpty()) {
            log.error("Account not found for account number {}", accountNumber);
            throw new BankingSystemException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        User u = account.get().getUser();
        if (!(Objects.equals(u.getUsername(), username) && Objects.equals(u.getPassword(), password))) {
            throw new BankingSystemException(ErrorCode.WRONG_CREDENTIALS);
        }

        return account.get();
    }


    public List<TransactionResponse> getTransactions(String accountNumber, String username, String password) {
        Optional<Account> account = accountDao.findByAccountNumber(accountNumber);
        if (account.isEmpty()) {
            throw new BankingSystemException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        User u = account.get().getUser();
        if (!(Objects.equals(u.getUsername(), username) && Objects.equals(u.getPassword(), password))) {
            throw new BankingSystemException(ErrorCode.WRONG_CREDENTIALS);
        }
        List<Transaction> list = transactionDao.finAllByAccountNumber(accountNumber);
        System.out.println(list);
        List<TransactionResponse> list1 = list.stream().map(t -> {
            TransactionResponse tr = new TransactionResponse();
            tr.setTransactionId(t.transactionId);
            tr.setAccountNumber(t.account.getAccountNumber());
            tr.setAmount(t.getAmount());
            tr.setOperation(t.getOperation());
            tr.setCreatedAt(t.getCreatedAt());
            return tr;
        }).collect(Collectors.toList());

        return list1;
    }
}
