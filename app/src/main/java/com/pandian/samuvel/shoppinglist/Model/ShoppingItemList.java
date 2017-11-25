package com.pandian.samuvel.shoppinglist.Model;

import java.io.Serializable;

/**
 * Created by HP PC on 02-11-2017.
 */

public class ShoppingItemList implements Serializable {
    String itemName;
    String owner;

    public ShoppingItemList() {
    }

    public ShoppingItemList(String itemName, String owner) {
        this.itemName = itemName;
        this.owner = owner;
    }

    public String getItemName() {
        return itemName;
    }

    public String getOwner() {
        return owner;
    }
}
