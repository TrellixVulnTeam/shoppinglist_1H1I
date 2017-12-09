package com.pandian.samuvel.shoppinglist.Activities.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pandian.samuvel.shoppinglist.Activities.MainActivity;
import com.pandian.samuvel.shoppinglist.R;

public class SignInActivity extends AppCompatActivity {
    EditText mUserNameEditText;
    EditText mPasswordEditText;
    Button mSignInButton;
    TextView mSignUpTextView;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initUI();
        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mUserNameEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                mAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Please check the username and password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,CreateAccountActivity.class));
                finish();
            }
        });
    }
    private void initUI(){
        mUserNameEditText = findViewById(R.id.signInUserNameET);
        mPasswordEditText = findViewById(R.id.signInPasswordET);
        mSignInButton = findViewById(R.id.signInBtn);
        mSignUpTextView = findViewById(R.id.signUpTv);
        mAuth = FirebaseAuth.getInstance();
    }
}
