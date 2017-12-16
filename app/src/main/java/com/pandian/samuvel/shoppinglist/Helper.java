package com.pandian.samuvel.shoppinglist;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pandian.samuvel.shoppinglist.Model.ShoppingItemList;
import com.pandian.samuvel.shoppinglist.Model.ShoppingList;
import com.pandian.samuvel.shoppinglist.Model.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HP PC on 28-10-2017.
 */

public class Helper {
    private static FirebaseAuth auth = FirebaseAuth.getInstance();


    public static void createShoppingList(String listName, HashMap<String, Long> timeStampCreated,String ownerName) {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setListName(listName);
        shoppingList.setOwnerName(ownerName);
        shoppingList.setTimeStampCreated(timeStampCreated);
        FirebaseDatabase.getInstance().getReference().child("activeList").child(auth.getUid()).push().setValue(shoppingList);
    }

    public static void updateShoppingList(String key, String editedListName, HashMap<String, Long> timeStampLastchanged, HashMap<String, Long> timeStampCreated,String ownerName) {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setListName(editedListName);
        shoppingList.setOwnerName(ownerName);
        shoppingList.setTimeStampCreated(timeStampCreated);
        shoppingList.setTimeStampLastUpdated(timeStampLastchanged);
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, shoppingList);
        FirebaseDatabase.getInstance().getReference().child("activeList").child(auth.getUid()).updateChildren(map);
    }

    public static void removeShoppingList(String key) {
        FirebaseDatabase.getInstance().getReference().child("activeList").child(key).removeValue();
    }

    public static void addListItem(String key, String itemName,String ownerName) {
        ShoppingItemList shoppingItemList = new ShoppingItemList();
        shoppingItemList.setItemName(itemName);
        shoppingItemList.setOwner(ownerName);
        FirebaseDatabase.getInstance().getReference().child("shoppingListItems").child(key).push().setValue(shoppingItemList);
    }

    public static void removeListItem(String groupKey, String key) {
        FirebaseDatabase.getInstance().getReference().child("shoppingListItems").child(groupKey).child(key).removeValue();
    }

    public static Task<ArrayList<User>> getUsers() {
        final ArrayList<User> userList = new ArrayList<>();
        final TaskCompletionSource<ArrayList<User>> tcs = new TaskCompletionSource<>();

        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        user.setUid(snapshot.getKey());
                        userList.add(user);
                    }
                    tcs.setResult(userList);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                tcs.setException(databaseError.toException());
            }
        });
        return tcs.getTask();
    }
    public static Task<User> getCurrentUser(final String uid){
        final User user = new User();
        final TaskCompletionSource<User> tcs = new TaskCompletionSource<>();
        Helper.getUsers().continueWith(new Continuation<ArrayList<User>, Object>() {
            @Override
            public Object then(@NonNull Task<ArrayList<User>> task) throws Exception {
                ArrayList<User> userList = task.getResult();
                for(int i=-0;i<userList.size();i++){
                    if(userList.get(i).getUid().equals(uid)){
                         tcs.setResult(userList.get(i));
                         break;
                    }
                }
                return tcs.getTask();
            }
        });
        return tcs.getTask();
    }

    /*public static ArrayList<User> getCurrentUser(String uid) {
        final ArrayList<User> userList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.getValue()!=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        user.setUid(snapshot.getKey());
                        userList.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return userList;
    }*/
}
