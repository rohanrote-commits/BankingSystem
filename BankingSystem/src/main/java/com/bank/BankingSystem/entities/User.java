package com.bank.BankingSystem.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.data.annotation.Id;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
public class User {
    @Id
    public String username;
    public String name;
    public String email;
    public String password;

    @JsonManagedReference
    public List<Account> accounts = new ArrayList<>();


    public void setAccount(List<Account> accounts) {
        this.accounts = (accounts == null) ? new ArrayList<>() : new ArrayList<>(accounts);
    }

    public void addAccount(Account account) {
        if (account == null) return;
        if (this.accounts == null) {
            this.accounts = new ArrayList<>();
        }
        this.accounts.add(account);
        account.setUser(this);
    }

    public void removeAccount(Account account) {
        if (account == null || this.accounts == null) return;
        this.accounts.remove(account);
        if (account.getUser() == this) {
            account.setUser(null);
        }
    }





}
