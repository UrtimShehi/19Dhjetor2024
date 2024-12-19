package com.example.a19dhjetor2024;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText usernameInput, emailInput, passwordInput;
    private Button signUpButton;
    private TextView alreadyHaveAccount;
    private DB databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinactivity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize database helper
        databaseHelper = new DB(this);


        // Initialize views
        usernameInput = findViewById(R.id.etUsername);
        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);

        signUpButton = findViewById(R.id.btnSignUp);
        alreadyHaveAccount = findViewById(R.id.tvAlreadyAccount);

        // Set up button click listeners
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSignUp();
            }
        });

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Validate input and perform sign-up logic
    private void validateAndSignUp() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            usernameInput.setError("Username is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Invalid email format");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        // Insert into database
        if (databaseHelper.insertAdminUser(username, email, password)) {
            Toast.makeText(this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
            // Navigate to Login screen after successful sign-up
            Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Sign-Up Failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}