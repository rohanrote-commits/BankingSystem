package com.bank.BankingSystem.dao;

import com.bank.BankingSystem.entities.Account;
import com.bank.BankingSystem.entities.AccountType;
import com.bank.BankingSystem.entities.User;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class AccountDaoImpl implements AccountsDao{

    private final DBCollection accountCollection;

    public AccountDaoImpl( @Qualifier("bankAccountCollection")DBCollection accountCollection) {
        this.accountCollection = accountCollection;
    }




    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        DBObject query = new BasicDBObject("_id", accountNumber);
        if (accountCollection.findOne(query) != null) {
            return true;
        }

        return false;
    }

    @Override
    public long countByUserUsername(String username) {
        DBObject query = new BasicDBObject("user.username", username);
        if (accountCollection.count(query) > 0) {
            return accountCollection.count(query);
        }
        return 0;
    }

    @Override
    public boolean existsById(String candidate) {
        DBObject query = new BasicDBObject("_id", candidate);
        if (accountCollection.findOne(query) != null) {
            return true;
        }
        return false;
    }

    @Override
    public Account save(Account account) {

        DBObject dbObject = new BasicDBObject("_id", account.getAccountNumber())
                .append("user", toUserDBObject(account.getUser()))
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
        WriteResult result = accountCollection.remove(query);
        System.out.println("Deleted accounts: " + result.getN());


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
        if (userObj instanceof DBObject) {
            account.setUser(fromUserDBObject((DBObject) userObj));
        } else {
            account.setUser(null);
        }

        return account;
    }

    private static DBObject toUserDBObject(User user) {
        if (user == null) return null;
        BasicDBObject dbo = new BasicDBObject();

        dbo.append("username", user.getUsername());
        dbo.append("password", user.getPassword());
        return dbo;
    }

    private static User fromUserDBObject(DBObject dbo) {
        if (dbo == null) return null;
        User u = new User();
        u.setUsername((String) dbo.get("username"));
        u.setPassword((String) dbo.get("password"));

        return u;
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
