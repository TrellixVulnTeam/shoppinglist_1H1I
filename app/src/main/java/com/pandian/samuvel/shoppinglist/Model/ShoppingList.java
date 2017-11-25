package com.pandian.samuvel.shoppinglist.Model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by HP PC on 29-10-2017.
 */

public class ShoppingList implements Serializable{
    String key;
    String listName;
    String ownerName;
    HashMap<String,Long> timeStampCreated;
    HashMap<String,Long> timeStampLastUpdated;

    public ShoppingList() {
    }

    public ShoppingList(String listName, String ownerName, HashMap<String,Long> timeStampCreated,HashMap<String,Long> timeStampLastUpdated,String key) {
        this.listName = listName;
        this.ownerName = ownerName;
        this.timeStampCreated = timeStampCreated;
        this.timeStampLastUpdated = timeStampLastUpdated;
        this.key =key;
    }

    public String getListName() {
        return listName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public HashMap<String, Long> getTimeStampCreated() {
        return timeStampCreated;
    }
    public HashMap<String,Long> getTimeStampLastUpdated(){
        return timeStampLastUpdated;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setTimeStampCreated(HashMap<String, Long> timeStampCreated) {
        this.timeStampCreated = timeStampCreated;
    }

    public void setTimeStampLastUpdated(HashMap<String, Long> timeStampLastUpdated) {
        this.timeStampLastUpdated = timeStampLastUpdated;
    }
}
