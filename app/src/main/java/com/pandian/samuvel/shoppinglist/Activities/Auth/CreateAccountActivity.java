package com.pandian.samuvel.shoppinglist.Activities.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.pandian.samuvel.shoppinglist.Activities.MainActivity;
import com.pandian.samuvel.shoppinglist.Model.User;
import com.pandian.samuvel.shoppinglist.R;

import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {
    EditText mEmailEt,mPasswordEt,mUserName,mPhoneNuber;
    Button mCreateAccBtn;
    TextView mSignUpTv;
    ProgressBar mProgressBar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initInstance();
        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(CreateAccountActivity.this,MainActivity.class));
        }
        mCreateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                String userName = mUserName.getText().toString().trim();
                String phoneNumber = mPhoneNuber.getText().toString().trim();
                if(password.length()<6 && password.length()>0){
                    Toast.makeText(getApplicationContext(),"Password Size is less than 6",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Email address is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Password is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                registerUser(email,password,userName,phoneNumber);
            }
        });
        mSignUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity.this,SignInActivity.class));
            }
        });
    }
    private void initInstance(){
        mEmailEt = findViewById(R.id.createAccEmailEt);
        mPasswordEt = findViewById(R.id.createAccPasswordEt);
        mUserName = findViewById(R.id.createAccUserNameET);
        mPhoneNuber = findViewById(R.id.createAccPhoneNumberEt);
        mCreateAccBtn = findViewById(R.id.createAccBtn);
        mSignUpTv = findViewById(R.id.signInAccTv);
        mProgressBar =findViewById(R.id.createAccPb);
        mAuth = FirebaseAuth.getInstance();
    }
    private void registerUser(final String email, String password, final String userName,final String phoneNumber){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete()) {
                    startActivity(new Intent(CreateAccountActivity.this,MainActivity.class));
                    HashMap<String,Object> users = new HashMap<>();
                    User user = new User();
                    user.setUserName(userName);
                    user.setPhoneNumber(phoneNumber);
                    user.setCreatedAt(ServerValue.TIMESTAMP);
                    users.put(mAuth.getUid(),user);
                    FirebaseDatabase.getInstance().getReference().child("users").setValue(users);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Authentication Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
