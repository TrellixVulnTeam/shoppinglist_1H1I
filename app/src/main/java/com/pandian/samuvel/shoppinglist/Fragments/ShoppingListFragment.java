package com.pandian.samuvel.shoppinglist.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pandian.samuvel.shoppinglist.Activities.ActiveShoppingListActivity;
import com.pandian.samuvel.shoppinglist.Activities.MainActivity;
import com.pandian.samuvel.shoppinglist.Adapter.ShoppingListAdapter;
import com.pandian.samuvel.shoppinglist.Model.ShoppingList;
import com.pandian.samuvel.shoppinglist.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment{

    ListView shoppingListview;
    int size =0;
    ShoppingListAdapter adapter;
    FirebaseAuth mAuth;
    public ShoppingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        initializeScreen(rootView);
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setProgress(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
        //checkChildIsAvailable(dialog);
        if(checkChildIsAvailable(dialog)) {
            getShoppingList(dialog);
        }
        return rootView;
    }

    public void initializeScreen(View rootView){
        shoppingListview = rootView.findViewById(R.id.shopingListLv);
        mAuth = FirebaseAuth.getInstance();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token",token);
    }

    private boolean checkChildIsAvailable(final ProgressDialog dialog){
        final boolean[] check = {true};
        FirebaseDatabase.getInstance().getReference().child("activeList").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    check[0] = false;
                    dialog.dismiss();
                    Toast.makeText(getContext(),"List is empty!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return check[0];
    }

    public void getShoppingList(final ProgressDialog dialog){
        final ArrayList<ShoppingList> list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("activeList").child(mAuth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null && dataSnapshot.getValue()!= null) {
                    dialog.dismiss();
                    final ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                    shoppingList.setKey(dataSnapshot.getKey());
                    list.add(shoppingList);
                    if(getContext()!=null){
                        adapter = new ShoppingListAdapter(list,getContext());
                        shoppingListview.setAdapter(adapter);
                    }

                    size = list.size();
                    shoppingListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(getContext(),ActiveShoppingListActivity.class);
                            intent.putExtra("selectedList",list.get(i));
                            startActivity(intent);
                        }
                    });
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                dialog.dismiss();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                shoppingList.setKey(dataSnapshot.getKey());
                for(ShoppingList shoppingList1 : list){
                    if(shoppingList.getKey().equals(shoppingList1.getKey())){
                        list.remove(shoppingList1);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
            }
        });

    }

}
