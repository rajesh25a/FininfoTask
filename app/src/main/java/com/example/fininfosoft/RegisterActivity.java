package com.example.fininfosoft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.fininfosoft.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        binding.loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntentToLogin();
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();

                // validateCredentials(,password);
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                } else if (email.length()<10) {
                    Toast.makeText(getApplicationContext(), "Username must be 10 characters", Toast.LENGTH_LONG).show();

                } else if (password.length()<7 || isStrongPassword(password)) {
                    Toast.makeText(getApplicationContext(), "Password must be 7 characters with one uppercase alphabet, one special character, and one numeric", Toast.LENGTH_LONG).show();

                }
                binding.progreesBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                binding.progreesBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Account created... ", Toast.LENGTH_LONG).show();
                                    sendIntentToLogin();
                                } else {
                                    //System.out.println("Registratio failure Error --------->  " + task.getException().getMessage());
                                    Toast.makeText(RegisterActivity.this, "Account creation  Failed..." + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    private void validateCredentials(String userName, String password) {
        if (userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_LONG).show();
        } else if (userName.length() != 10) {
            Toast.makeText(this, "Username must be 10 characters", Toast.LENGTH_LONG).show();
        } else if (password.length() < 7 || !isStrongPassword(password)) {
            Toast.makeText(this, "Password must be 7 characters with one uppercase alphabet, one special character, and one numeric", Toast.LENGTH_LONG).show();
        } else {
            // Call Firebase authentication method
            // createUserWithUsernamePAssword(userName,password);
        }
    }

    private boolean isStrongPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).*$");

    }

    private void sendIntentToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}