package com.pandian.samuvel.shoppinglist.Model;

/**
 * Created by samuvelp on 02/12/17.
 */

public class User {
    String userName;

    public User() {
    }

    public User(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
