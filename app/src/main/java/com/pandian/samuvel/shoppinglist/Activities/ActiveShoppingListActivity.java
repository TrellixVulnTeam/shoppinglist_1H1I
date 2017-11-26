package com.pandian.samuvel.shoppinglist.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pandian.samuvel.shoppinglist.Adapter.ListItemAdapter;
import com.pandian.samuvel.shoppinglist.Helper;
import com.pandian.samuvel.shoppinglist.Model.ShoppingItemList;
import com.pandian.samuvel.shoppinglist.Model.ShoppingList;
import com.pandian.samuvel.shoppinglist.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ActiveShoppingListActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editList,addItemText;
    AlertDialog editDialog,removeDialog,addItemDialog;
    Intent intent;
    FloatingActionButton addItemFab;
    ShoppingList shoppingList;
    ListItemAdapter listItemAdapter;
    ListView addedListItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_shopping_list);

        toolbar = findViewById(R.id.activeShoppingListToolbar);
        intent = getIntent();
        shoppingList = (ShoppingList) intent.getSerializableExtra("selectedList");
        addItemFab = findViewById(R.id.addItemFab);
        addedListItem = findViewById(R.id.addedItemList);

        LayoutInflater inflater = LayoutInflater.from(this);
        View editPromptView = inflater.inflate(R.layout.prompt_edit_list,null);
        View addListItemView =inflater.inflate(R.layout.prompt_create_listitem,null);
        setEditPromptView(editPromptView);
        setAddItemPrompView(addListItemView);
        setSupportActionBar(toolbar);
        setActivityTitle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setProgress(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
        if(checkListIsAvailable(dialog)) {
            loadListItem(dialog);
        }
        addItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemDialog.show();
            }
        });

    }

    private boolean checkListIsAvailable(final ProgressDialog dialog) {
        final boolean[] check = {true};
        FirebaseDatabase.getInstance().getReference().child("shoppingListItems").child(shoppingList.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    check[0] = false;
                    dialog.dismiss();
                    Toast.makeText(ActiveShoppingListActivity.this, "List is empty!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return check[0];
    }

    private void loadListItem(final ProgressDialog dialog){
        FirebaseDatabase.getInstance().getReference().child("shoppingListItems").child(shoppingList.getKey()).addChildEventListener(new ChildEventListener() {
            final ArrayList<ShoppingItemList> list = new ArrayList<>();
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null && dataSnapshot.getChildren()!=null){
                    ShoppingItemList shoppingItemList = dataSnapshot.getValue(ShoppingItemList.class);
                    shoppingItemList.setKey(dataSnapshot.getKey());
                    list.add(shoppingItemList);
                    listItemAdapter = new ListItemAdapter(list,ActiveShoppingListActivity.this,shoppingList.getKey());
                    addedListItem.setAdapter(listItemAdapter);
                    dialog.dismiss();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final ShoppingItemList shoppingItemList = dataSnapshot.getValue(ShoppingItemList.class);
                shoppingItemList.setKey(dataSnapshot.getKey());
                for(ShoppingItemList shoppingItemList1 : list){
                    if(shoppingItemList.getKey().equals(shoppingItemList1.getKey())){
                        list.remove(shoppingItemList1);
                        listItemAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setAddItemPrompView(View addListItemView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(addListItemView);
        addItemText = addListItemView.findViewById(R.id.addItemListEditText);
        builder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!addItemText.equals("")){
                    Helper.addListItem(shoppingList.getKey(),addItemText.getText().toString());
                    addItemText.setText("");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        addItemDialog = builder.create();
    }

    private void setEditPromptView(View editPromptView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(editPromptView);
        editList = editPromptView.findViewById(R.id.editListEt);
        builder.setPositiveButton("Update List", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String editedListName = editList.getText().toString();
                HashMap<String,Long> timeStampLastchanged = new HashMap<>();
                timeStampLastchanged.put("timeStamp",System.currentTimeMillis());
                Helper.updateShoppingList(shoppingList.getKey(),editedListName,timeStampLastchanged,shoppingList.getTimeStampCreated());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        editDialog = builder.create();
    }


    private void setActivityTitle(){
        final ArrayList<ShoppingList> list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("activeList").child(shoppingList.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.getValue()!=null) {
                    ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                    getSupportActionBar().setTitle(shoppingList.getListName());
                    //timeStampCreatedLong = list.get(0).getTimeStamp().get("timeStamp");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showRemoveListPrompt(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove this list?");
        builder.setTitle("Remove List");
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Helper.removeShoppingList(shoppingList.getKey());
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        removeDialog = builder.create();
        removeDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.editShoppingList:
                editDialog.show();
                return true;
            case R.id.removeShoppingList:
                showRemoveListPrompt();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_menu,menu);
        return true;
    }
}
