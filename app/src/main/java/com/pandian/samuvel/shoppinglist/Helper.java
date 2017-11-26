package com.pandian.samuvel.shoppinglist;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pandian.samuvel.shoppinglist.Model.ShoppingItemList;
import com.pandian.samuvel.shoppinglist.Model.ShoppingList;

import java.util.HashMap;

/**
 * Created by HP PC on 28-10-2017.
 */

public class Helper
{

    public static void createShoppingList(String listName, HashMap<String, Long> timeStampCreated) {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setListName(listName);
        shoppingList.setOwnerName("Anonymous");
        shoppingList.setTimeStampCreated(timeStampCreated);
        FirebaseDatabase.getInstance().getReference().child("activeList").push().setValue(shoppingList);
    }

    public static void updateShoppingList(String key, String editedListName, HashMap<String, Long> timeStampLastchanged, HashMap<String,Long> timeStampCreated) {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setListName(editedListName);
        shoppingList.setOwnerName("Anonymous");
        shoppingList.setTimeStampCreated(timeStampCreated);
        shoppingList.setTimeStampLastUpdated(timeStampLastchanged);
        HashMap<String,Object> map = new HashMap<>();
        map.put(key,shoppingList);
        FirebaseDatabase.getInstance().getReference().child("activeList").updateChildren(map);
    }

    public static void removeShoppingList(String key) {
        FirebaseDatabase.getInstance().getReference().child("activeList").child(key).removeValue();
    }

    public static void addListItem(String key,String itemName){
        ShoppingItemList shoppingItemList = new ShoppingItemList();
        shoppingItemList.setItemName(itemName);
        shoppingItemList.setOwner("Anonymous");
        FirebaseDatabase.getInstance().getReference().child("shoppingListItems").child(key).push().setValue(shoppingItemList);
    }
    public static void removeListItem(String groupKey,String key){
        FirebaseDatabase.getInstance().getReference().child("shoppingListItems").child(groupKey).child(key).removeValue();
    }
}
