package com.bank.BankingSystem.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
@Document(collection = "user")
public class User {
    @Id
    public String username;
    public String name;
    public String email;
    public String password;

    @DBRef
    @JsonManagedReference
    public List<Account> accounts = new ArrayList<>();



}
