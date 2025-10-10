package com.bank.BankingSystem.config;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class MongoConfig {

    @Value("${mongo.host}")
    private String host;

    @Value("${mongo.port}")
    private int port;

    @Value("${mongo.database}")
    private String database;

    @Value("${mongo.username}")
    private String username;

    @Value("${mongo.password}")
    private String password;

    @Value("${mongo.authdb}")
    private String authDb;

    @Bean
    public MongoClient mongoClient() {
        String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);
        String uri = String.format(
                "mongodb://%s:%s@%s:%d/%s?authSource=%s",
                username, encodedPassword, host, port, database, authDb
        );
        System.out.println("Mongo URI: " + uri);


        return new MongoClient(new MongoClientURI(uri));
    }

    @Bean
    public DB mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDB(database);
    }

    @Bean
    @Qualifier("userCollection")
    public DBCollection userCollection(DB mongoDatabase) {
        return mongoDatabase.getCollection("user");
    }

    @Bean
    @Qualifier("bankAccountCollection")
    public DBCollection bankAccountCollection(DB mongoDatabase) {
        return mongoDatabase.getCollection("accounts");
    }
    @Bean
    @Qualifier("transactionCollection")
    public DBCollection transactionCollection(DB mongoDatabase) {
        return mongoDatabase.getCollection("transactions");
    }
}
