package com.bank.BankingSystem.config;


import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;

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
        return MongoClients.create(uri);
    }

    @Bean
    public MongoDatabase getDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(database);
    }

    @Bean
    public DBCollection userCollection(MongoClient mongoClient) {
        DB db = (DB) mongoClient.getDatabase(database);
        return db.getCollection("user");
    }


}
