package com.example.a19dhjetor2024;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class verifyOtpActivity extends AppCompatActivity {
    LoginActivity lg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_otp_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        lg = new LoginActivity();

        EditText otpEditText = findViewById(R.id.otpEditText);
        Button verifyOtpButton = findViewById(R.id.verifyOtpButton);
        Button resentOtpButton = findViewById(R.id.resendOtp);

        String generatedOtp = getIntent().getStringExtra("OTP");
        String email = getIntent().getStringExtra("email");

        verifyOtpButton.setOnClickListener(v -> {
            String enteredOtp = otpEditText.getText().toString();

            if (enteredOtp.equals(generatedOtp)) {
                Intent intent = new Intent(verifyOtpActivity.this, ActivityMain.class);
                startActivity(intent);
            } else {
                Toast.makeText(verifyOtpActivity.this, "Kodi OTP është i pasaktë!", Toast.LENGTH_SHORT).show();
            }
        });
        resentOtpButton.setOnClickListener(view -> {
            String otp =  lg.generateOtp();
            new Thread(() -> {
                try {
                    EmailSender.sendEmail(
                            email,
                            "Your OTP Code",
                            "Your OTP code is: " + otp
                    );
                    // Show success toast
                    runOnUiThread(() -> Toast.makeText(verifyOtpActivity.this, "OTP has been sent to your email!", Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(verifyOtpActivity.this, "Error sending OTP email.", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });

    }
}