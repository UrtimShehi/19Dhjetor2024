package com.example.a19dhjetor2024;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, signupButton;
    private DB databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.loginactivity);

        // Initialize database helper
        databaseHelper = new DB(this);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupbutton);

        // Set up login button click listener
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate fields
            if (email.isEmpty()) {
                emailEditText.setError("Email is required");
                return;
            }

            if (password.isEmpty()) {
                passwordEditText.setError("Password is required");
                return;
            }

            // Validate credentials with the database
            if (isValidCredentials(email, password)) {
                // Generate OTP
                String otp = generateOtp();

                // Send OTP via email
                new Thread(() -> {
                    try {
                        EmailSender.sendEmail(
                                email,
                                "Your OTP Code",
                                "Your OTP code is: " + otp
                        );
                        // Show success toast
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "OTP has been sent to your email!", Toast.LENGTH_SHORT).show());
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error sending OTP email.", Toast.LENGTH_SHORT).show());
                    }
                }).start();

                // Proceed to OTP verification screen
                Intent intent = new Intent(LoginActivity.this, verifyOtpActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("OTP", otp);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Incorrect email or password!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up sign-up button click listener
        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    // Method to validate credentials with the database
    private boolean isValidCredentials(String email, String password) {
        return databaseHelper.isUserValid(email, password);
    }

    // Method to generate a random OTP
    public String generateOtp() {
        return String.format("%06d", (int) (Math.random() * 10000));
    }


}