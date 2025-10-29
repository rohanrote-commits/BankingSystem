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
import java.util.Date;
import java.util.List;

@Component
public class TransactionDaoImpl implements TransactionDao {

    private final DBCollection transactionCollection;

    public TransactionDaoImpl(@Qualifier("transactionCollection") DBCollection transactionCollection) {
        this.transactionCollection = transactionCollection;
    }

    @Override
    public Transaction save(Transaction transaction) {
        DBObject dbObject = new BasicDBObject("accountNumber", transaction.getAccountNumber())
                .append("amount", transaction.getAmount())
                .append("createdAt", toDate(transaction.getCreatedAt()))
                .append("operation", transaction.getOperation() != null ? transaction.getOperation().toString() : null)
                .append("transferdToAccountNumber", transaction.getTransferdToAccountNumber());

        transactionCollection.insert(dbObject);
        Object id = dbObject.get("_id");
        if (id != null) {
            transaction.setTransactionId(id.toString());
        }
        return transaction;
    }

    @Override
    public List<Transaction> finAllByAccountNumber(String accountNumber) {
        DBObject query = new BasicDBObject("accountNumber", accountNumber);
        List<DBObject> result = transactionCollection.find(query).toArray();
        if (result != null && !result.isEmpty()) {
            return result.stream().map(this::dbObjectToTransaction).toList();
        }
        return List.of();
    }

    @Override
    public void deleteAllByAccountNumber(String accountNumber) {
        DBObject query = new BasicDBObject("accountNumber", accountNumber);
        transactionCollection.remove(query);
    }

    private Transaction dbObjectToTransaction(DBObject dbObject) {
        Transaction transaction = new Transaction();
        Object id = dbObject.get("_id");
        if (id != null) transaction.setTransactionId(id.toString());

        Object s = dbObject.get("operation");
        if (s instanceof String) transaction.setOperation(Operations.valueOf((String) s));

        transaction.setAccountNumber((String) dbObject.get("accountNumber"));
        transaction.setCreatedAt(fromDate((Date) dbObject.get("createdAt")));

        Object amt = dbObject.get("amount");
        if (amt instanceof Number) transaction.setAmount(((Number) amt).doubleValue());

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
}
