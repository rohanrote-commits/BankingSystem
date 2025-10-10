package com.bank.BankingSystem.dao;

import com.bank.BankingSystem.entities.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class TransactionDaoImpl implements TransactionDao{

    private final DBCollection transactionCollection;

    public TransactionDaoImpl( @Qualifier("transactionCollection")DBCollection transactionCollection) {
        this.transactionCollection = transactionCollection;
    }


    @Override
    public Transaction save(Transaction transaction) {
        DBObject dbObject = new BasicDBObject("account", accountToDbObject(transaction.getAccount()))
                .append("amount", transaction.getAmount())
                .append("createdAt", toDate(transaction.getCreatedAt()))
                .append("operation", transaction.getOperation().toString())
                .append("transferdToAccountNumber",transaction.getTransferdToAccountNumber());
        transactionCollection.insert(dbObject);
        List<BasicDBObject> list = new ArrayList<>();


        Object id = dbObject.get("_id");
        transaction.setTransactionId(id.toString());
        return transaction;
    }



    @Override
    public List<Transaction> finAllByAccountNumber(String accountNumber) {
        DBObject query = new BasicDBObject("account._id", accountNumber);
        List<DBObject> result = transactionCollection.find(query).toArray();
        if (result != null) {
         List<Transaction> transactions = result.stream().map(this::dbObjectToTransaction).toList();
         return transactions;
        }
        return List.of();
    }

    @Override
    public void deleteAllByAccountNumber(String accountNumber) {
        DBObject query = new BasicDBObject("account._id", accountNumber);
        transactionCollection.remove(query);
    }

    private Transaction dbObjectToTransaction(DBObject dbObject){
        Transaction transaction = new Transaction();
        transaction.setTransactionId((String) dbObject.get("_id").toString());
        Object s =  dbObject.get("operation");
        if (s instanceof String) {
            transaction.setOperation(Operations.valueOf((String) s));
        }


        transaction.setAccount((Account) dbObjectToAccount((DBObject) dbObject.get("account")));
        transaction.setCreatedAt((LocalDateTime) fromDate((Date) dbObject.get("createdAt")));
        transaction.setAmount((Double) dbObject.get("amount"));
        transaction.setTransferdToAccountNumber((String) dbObject.get("transferdToAccountNumber"));
        return transaction;
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
    private static User fromUserDBObject(DBObject dbo) {
        if (dbo == null) return null;
        User u = new User();
        u.setUsername((String) dbo.get("username"));
        u.setPassword((String) dbo.get("password"));

        return u;
    }
    private DBObject userToDbObject(User user){
        DBObject dbObject = new BasicDBObject();
        dbObject.put("_id", user.getUsername());
        dbObject.put("name", user.getName());
        dbObject.put("email", user.getEmail());
        dbObject.put("password", user.getPassword());
        List<Account> accounts = user.getAccounts();
        List<DBObject> accountsDbObject = accounts.stream().map(this::accountToDbObject).toList();
        dbObject.put("accounts", accountsDbObject);
        dbObject.put("_class",user.getClass().getName());
        return dbObject;
    }

    private DBObject accountToDbObject(Account account){
        DBObject dbObject = new BasicDBObject();
        dbObject.put("_id", account.getAccountNumber());
        dbObject.put("accountType", account.getAccountType().toString());
        dbObject.put("createdAt", account.getCreatedAt());
        dbObject.put("updatedAt", account.getUpdatedAt());
        dbObject.put("balance", account.getBalance());
        dbObject.put("user", userToDbObject(account.getUser()));
        return dbObject;
    }
}
