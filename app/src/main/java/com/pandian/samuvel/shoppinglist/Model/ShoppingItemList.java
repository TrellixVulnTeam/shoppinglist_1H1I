package com.pandian.samuvel.shoppinglist.Model;

import java.io.Serializable;

/**
 * Created by HP PC on 02-11-2017.
 */

public class ShoppingItemList implements Serializable {
    String itemName;
    String owner;
    String key;

    public ShoppingItemList() {
    }

    public ShoppingItemList(String key,String itemName, String owner) {
        this.key = key;
        this.itemName = itemName;
        this.owner = owner;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getItemName() {
        return itemName;
    }

    public String getOwner() {
        return owner;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
