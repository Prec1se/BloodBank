package com.example.bloodhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.base.MoreObjects;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private TextView backButton, forgotPassword;
    private Button loginButton;
    private TextInputEditText loginEmail, loginPassword;

    private ProgressDialog loader;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener authStateListener;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        backButton = findViewById(R.id.backButton);
//        loginEmail = findViewById(R.id.registerEmail);
//        loginPassword = findViewById(R.id.password);
//        forgotPassword = findViewById(R.id.forgotPassword);
//        loginButton = findViewById(R.id.loginButton);
//
//        loader = new ProgressDialog(this);
//        mAuth = FirebaseAuth.getInstance();
//
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = mAuth.getCurrentUser();
//                if(user != null) {
//                    Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        };
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String email = loginEmail.getText().toString().trim();
//                final String password = loginPassword.getText().toString().trim();
//
//                if(TextUtils.isEmpty(email)) {
//                    loginEmail.setError("Email is required!");
//                }
//                if(TextUtils.isEmpty(password)) {
//                    loginPassword.setError("Password is required!");
//                }
//
//                else {
//                    loader.setMessage("Login in progress");
//                    loader.setCanceledOnTouchOutside(false);
//                    loader.show();
//
//                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(task.isSuccessful()) {
//                                Toast.makeText(LoginActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                            else {
//                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                            }
//                            loader.dismiss();
//                        }
//                    });
//                }
//            }
//        });
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(authStateListener);
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        mAuth.addAuthStateListener(authStateListener);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

//        getSupportActionBar().setTitle("Sign in");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginEmail = findViewById(R.id.registerEmail);
        loginPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        backButton = findViewById(R.id.backButton);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            finish();
            Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
            startActivity(intent);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

    }

//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public void onClick(View v){
//        switch (v.getId()){
//            case R.id.loginButton:
//                signIn();
//                break;
//            case R.id.backButton:
//                startActivity(new Intent(this, RegistrationActivity.class));
//                break;
//        }
//    }

    private void signIn() {

        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if(email.isEmpty()){
            loginEmail.setError("Enter your email address");
            loginEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmail.setError("Provide a valid email address");
            loginEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            loginPassword.setError("Enter your email address");
            loginPassword.requestFocus();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Please wait...");
        pd.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

//                        if(firebaseUser.isEmailVerified()){
//                            Toast.makeText(LoginActivity.this, "Signed in successfully. enjoy your stay!",Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(this, HomepageActivity.class));
//                        }
//                        else{
//                            checkIfEmailVerified();
//                        }

                        startActivity(new Intent(this, HomepageActivity.class));

                    }
                    else{
                        Toast.makeText(LoginActivity.this,
                                "Oops! These credentials are not connected to an account",
                                Toast.LENGTH_LONG).show();
                    }
                    pd.dismiss();
                });

    }

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            finish();
            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(LoginActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();

            //restart this activity
            overridePendingTransition(0, 0);
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());

        }
    }

}