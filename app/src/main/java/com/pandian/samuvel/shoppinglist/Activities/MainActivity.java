package com.pandian.samuvel.shoppinglist.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pandian.samuvel.shoppinglist.Activities.Auth.SignInActivity;
import com.pandian.samuvel.shoppinglist.Fragments.MealsFragment;
import com.pandian.samuvel.shoppinglist.Fragments.ShoppingListFragment;
import com.pandian.samuvel.shoppinglist.Helper;
import com.pandian.samuvel.shoppinglist.Model.User;
import com.pandian.samuvel.shoppinglist.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton fabButton;
    EditText userInputCreateList;
    FirebaseAuth mAuth;
    String ownerName ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.mainActivityToolBar);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        fabButton = findViewById(R.id.fab);
        mAuth = FirebaseAuth.getInstance();
        //ref = FirebaseDatabase.getInstance().getReference().child("activeList");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shop");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        LayoutInflater inflater = LayoutInflater.from(this);
        View promptView = inflater.inflate(R.layout.prompt_create_list,null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView);

        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    finish();
                }
            }
        };
        Helper.getCurrentUser(mAuth.getUid()).continueWith(new Continuation<User, Object>() {
            @Override
            public Object then(@NonNull Task<User> task) throws Exception {
                ownerName = task.getResult().getUserName();
                return null;
            }
        });
        userInputCreateList = promptView.findViewById(R.id.createListEt);
        builder.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String listName = userInputCreateList.getText().toString();
                        if(!listName.equals("")) {
                            //ref.setValue(listName);
                            HashMap<String,Long> timeStampCreated = new HashMap<>();
                            timeStampCreated.put("timeStamp",System.currentTimeMillis());
                            Helper.createShoppingList(listName,timeStampCreated,ownerName);
                            //ShoppingList shoppingList = new ShoppingList(listName,"Anonymous",timeStampCreated);
                            //Helper.addShoppingList(shoppingList);
                            userInputCreateList.setText("");
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Enter something!",Toast.LENGTH_SHORT).show();

                    }
                });
        final AlertDialog dialog = builder.create();
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ShoppingListFragment(),"ShoppingList");
        adapter.addFragment(new MealsFragment(),"Meals");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logOutUser();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    private void logOutUser(){
        mAuth.signOut();
        Intent intent = new Intent(this,SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
