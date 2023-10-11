package com.example.model;

import java.io.Serializable;

public class Admin implements Identifiable<String>, Serializable {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getID() {
        return username;
    }

    @Override
    public void setID(String s) {
        username=s;
    }
}
