package com.bank.BankingSystem.dao;

import com.bank.BankingSystem.entities.Account;
import com.bank.BankingSystem.entities.AccountType;
import com.bank.BankingSystem.entities.Transaction;
import com.bank.BankingSystem.entities.User;
import com.bank.BankingSystem.exceptions.BankingSystemException;
import com.bank.BankingSystem.exceptions.ErrorCode;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class UserDaoImpl implements UserDao {
    @Autowired
    private TransactionDaoImpl transactionDao;
    @Autowired
    AccountDaoImpl accountDao;

    private final DBCollection userCollection;

    public UserDaoImpl(DBCollection userCollection) {
        this.userCollection = userCollection;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        DBObject query = new BasicDBObject("_id", username);
        DBObject result = userCollection.findOne(query);
        if (result != null) {
            User user = dbObjectToUser(result);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        DBObject dbObject = userToDbObject(user);
        DBObject query = new BasicDBObject("_id", user.getUsername());
        userCollection.update(query, dbObject, true, false);
        return user;
    }

    @Override
    public Optional<User> deleteUserByUsernameAndPassword(String username, String password) {
        DBObject query = new BasicDBObject("_id", username).append("password", password);
        Optional<User> optionalUser = findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new BankingSystemException(ErrorCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        if (user.getAccounts() != null && !user.getAccounts().isEmpty()) {
            user.getAccounts().forEach(account -> {
                List<Transaction> transactions = transactionDao.finAllByAccountNumber(account);
                if (transactions != null && !transactions.isEmpty()) {
                    transactionDao.deleteAllByAccountNumber(account);
                }
            });
            accountDao.deleteAccountByUsername(username);
        }
        userCollection.remove(query);
        return optionalUser;
    }

    private User dbObjectToUser(DBObject dbObject) {
        User user = new User();
        user.setUsername((String) dbObject.get("_id"));
        user.setName((String) dbObject.get("name"));
        user.setEmail((String) dbObject.get("email"));
        user.setPassword((String) dbObject.get("password"));

        Object accountsObj = dbObject.get("accounts");
        if (accountsObj instanceof List<?>) {
            user.setAccounts(((List<?>) accountsObj).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList());
        } else {
            user.setAccounts(List.of());
        }
        return user;
    }

    private DBObject userToDbObject(User user) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("_id", user.getUsername());
        dbObject.put("name", user.getName());
        dbObject.put("email", user.getEmail());
        dbObject.put("password", user.getPassword());
        dbObject.put("accounts", user.getAccounts());
        dbObject.put("_class", user.getClass().getName());
        return dbObject;
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
