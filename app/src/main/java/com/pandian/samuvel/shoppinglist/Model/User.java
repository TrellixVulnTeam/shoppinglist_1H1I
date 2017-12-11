package com.pandian.samuvel.shoppinglist.Model;

import java.util.Map;

/**
 * Created by samuvelp on 02/12/17.
 */

public class User {
    String userName;
    String phoneNumber;
    Map<String, String> createdAt;
    String uid;
    public User() {
    }

    public User(String userName,String phoneNumber,Map<String, String>  createdAt,String uid) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Map<String, String> getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Map<String, String> createdAt) {
        this.createdAt = createdAt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
