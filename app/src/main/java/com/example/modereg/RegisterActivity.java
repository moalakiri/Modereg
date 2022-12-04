package com.example.modereg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.modereg.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
private EditText eTName, eTEmail, eTAddress, eTPassword;
private Button btnRegister;
private ProgressBar progressBar;

private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        eTName = (EditText) findViewById(R.id.ETName);
        eTEmail = (EditText) findViewById(R.id.ETEmail);
        eTAddress = (EditText) findViewById(R.id.ETAddress);
        eTPassword = (EditText) findViewById(R.id.ETPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            //handle logged in user
        }
    }

    private void registerUser(){
        String name= eTName.getText().toString().trim();
        String email= eTEmail.getText().toString().trim();
        String address= eTAddress.getText().toString().trim();
        String password= eTPassword.getText().toString().trim();

        if(name.isEmpty()){
            eTName.setError("Name is required");
            eTName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            eTEmail.setError("Email is required");
            eTEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            eTEmail.setError("Enter a valid Email");
            eTEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            eTPassword.setError("Password is required");
            eTPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            eTPassword.setError("Password should be atleast 6 characters long");
            eTPassword.requestFocus();
            return;
        }
        if(address.isEmpty()){
            eTAddress.setError("Address is required");
            eTAddress.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            User user = new User(
                                    name, email, address
                            );
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "You have registered successfully", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}