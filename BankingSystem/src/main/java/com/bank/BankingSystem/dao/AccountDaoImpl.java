package com.bank.BankingSystem.dao;

import com.bank.BankingSystem.entities.Account;
import com.bank.BankingSystem.entities.AccountType;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class AccountDaoImpl implements AccountsDao {

    private final DBCollection accountCollection;

    public AccountDaoImpl(@Qualifier("bankAccountCollection") DBCollection accountCollection) {
        this.accountCollection = accountCollection;
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        DBObject query = new BasicDBObject("_id", accountNumber);
        return accountCollection.findOne(query) != null;
    }

    @Override
    public long countByUserUsername(String username) {
        DBObject query = new BasicDBObject("user.username", username);
        return accountCollection.count(query);
    }

    @Override
    public boolean existsById(String candidate) {
        DBObject query = new BasicDBObject("_id", candidate);
        return accountCollection.findOne(query) != null;
    }

    @Override
    public Account save(Account account) {
        DBObject dbObject = new BasicDBObject("_id", account.getAccountNumber())
                .append("user", new BasicDBObject("username", account.getUsername()))
                .append("accountType", account.getAccountType() != null ? account.getAccountType().name() : null)
                .append("createdAt", toDate(account.getCreatedAt()))
                .append("updatedAt", toDate(account.getUpdatedAt()))
                .append("balance", account.getBalance());

        DBObject query = new BasicDBObject("_id", account.getAccountNumber());
        accountCollection.update(query, dbObject, true, false);
        return account;
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        BasicDBObject query = new BasicDBObject("_id", accountNumber);
        DBObject result = accountCollection.findOne(query);
        if (result != null) {
            Account account = dbObjectToAccount(result);
            return Optional.of(account);
        }
        return Optional.empty();
    }

    @Override
    public void deleteAccountByUsername(String username) {
        DBObject query = new BasicDBObject("user.username", username);
        accountCollection.remove(query);
    }

    public Account dbObjectToAccount(DBObject dbObject) {
        Account account = new Account();
        account.setAccountNumber((String) dbObject.get("_id"));

        Object at = dbObject.get("accountType");
        if (at instanceof String) {
            account.setAccountType(AccountType.valueOf((String) at));
        } else {
            account.setAccountType(null);
        }

        account.setCreatedAt(fromDate((Date) dbObject.get("createdAt")));
        account.setUpdatedAt(fromDate((Date) dbObject.get("updatedAt")));

        Object bal = dbObject.get("balance");
        if (bal instanceof Number) {
            account.setBalance(((Number) bal).doubleValue());
        } else {
            account.setBalance(null);
        }

        Object userObj = dbObject.get("user");
        if (userObj instanceof DBObject dbo) {
            account.setUsername((String) dbo.get("username"));
        } else {
            account.setUsername(null);
        }

        return account;
    }

    private static Date toDate(LocalDateTime ldt) {
        if (ldt == null) return null;
        Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    private static LocalDateTime fromDate(Date date) {
        if (date == null) return null;
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
