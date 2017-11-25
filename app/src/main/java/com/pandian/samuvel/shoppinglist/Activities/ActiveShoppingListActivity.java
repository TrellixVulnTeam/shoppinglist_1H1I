package com.pandian.samuvel.shoppinglist.Activities;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pandian.samuvel.shoppinglist.Helper;
import com.pandian.samuvel.shoppinglist.Model.ShoppingList;
import com.pandian.samuvel.shoppinglist.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ActiveShoppingListActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editList;
    AlertDialog editDialog,removeDialog;
    Intent intent;
    FloatingActionButton addItemFab;
    ShoppingList shoppingList;
    EditText userInputCreateList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_shopping_list);

        toolbar = findViewById(R.id.activeShoppingListToolbar);
        intent = getIntent();
        shoppingList = (ShoppingList) intent.getSerializableExtra("selectedList");
        addItemFab = findViewById(R.id.addItemFab);

        addItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        LayoutInflater inflater = LayoutInflater.from(this);
        View editPromptView = inflater.inflate(R.layout.edit_list_prompt,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(editPromptView);
        editList = editPromptView.findViewById(R.id.editListEt);
        builder.setPositiveButton("Update List", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String editedListName = editList.getText().toString();
                HashMap<String,Long> timeStampLastchanged = new HashMap<>();
                timeStampLastchanged.put("timeStamp",System.currentTimeMillis());
                //ShoppingList shoppingList = new ShoppingList(editedListName,"Anonymous",timeStampLastchanged);
                //Helper.updateShoppingList(shoppingList);
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
        setSupportActionBar(toolbar);
        setActivityTitle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                removeDialog.show();
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
