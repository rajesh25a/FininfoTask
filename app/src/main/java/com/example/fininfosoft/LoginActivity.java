package com.example.fininfosoft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.fininfosoft.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth!=null){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // The user is already signed in, go to MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.loginButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Fields cannot be empty ", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.length()<10 ) {
                    Toast.makeText(LoginActivity.this, "Fields cannot be empty ", Toast.LENGTH_SHORT).show();

                }
                // validateCredentials(email,password);
                binding.progreesBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                binding.progreesBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    sendIntentToMainScreen();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getApplicationContext(), "Invalid credentials ", Toast.LENGTH_LONG).show();
                                    binding.progreesBar.setVisibility(View.GONE);

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
            signInUserWithUserAndPassword(userName, password);
        }
    }

    private void signInUserWithUserAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                binding.progreesBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    //sendIntent to main activity
                    sendIntentToMainScreen();
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void sendIntentToMainScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isStrongPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).*$");
    }
}