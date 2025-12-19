package com.example.adsite.model;

public class AdOwner {
    private final String accountName;
    private final String password;

    public AdOwner(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getPassword() {
        return password;
    }
}
