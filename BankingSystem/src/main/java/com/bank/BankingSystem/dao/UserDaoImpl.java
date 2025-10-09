package com.bank.BankingSystem.dao;

import com.bank.BankingSystem.entities.Account;
import com.bank.BankingSystem.entities.User;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDaoImpl implements UserDao{

   private final DBCollection userCollection;
    public UserDaoImpl( MongoDatabase mongoDatabase) {

        this.userCollection = (DBCollection) mongoDatabase.getCollection("user");
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
        DBObject dbObject = new BasicDBObject();
        dbObject.put("_id", user.getUsername());
        dbObject.put("name", user.getName());
        dbObject.put("email", user.getEmail());
        dbObject.put("password", user.getPassword());
        dbObject.put("accounts", user.getAccounts());


        DBObject query = new BasicDBObject("_id", user.getUsername());
       userCollection.update(query, dbObject, true, false);

        return user;
    }

    @Override
    public Optional<User> deleteUserByUsernameAndPassword(String username, String password) {

        DBObject query = new BasicDBObject("_id", username).append("password", password);
        Optional<User> optionalUser = findByUsername(username);
        userCollection.remove(query);
        return optionalUser;

    }


    private User dbObjectToUser(DBObject dbObject) {
        User user = new User();
        user.setUsername((String) dbObject.get("_id"));
        user.setName((String) dbObject.get("name"));
        user.setEmail((String) dbObject.get("email"));
        user.setPassword((String) dbObject.get("password"));
        user.setAccounts((List<Account>) dbObject.get("accounts")); // cast appropriately
        return user;
    }
}
