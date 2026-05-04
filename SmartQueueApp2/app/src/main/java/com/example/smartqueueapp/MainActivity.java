package com.example.smartqueueapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText phoneInput, passwordInput;
    Button loginBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneInput    = findViewById(R.id.phoneInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn      = findViewById(R.id.loginBtn);
        progressBar   = findViewById(R.id.progressBar);

        loginBtn.setOnClickListener(v -> {
            String phone    = phoneInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (TextUtils.isEmpty(phone)) {
                phoneInput.setError("Enter phone number");
                return;
            }
            if (phone.length() < 10) {
                phoneInput.setError("Enter valid 10-digit number");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordInput.setError("Enter password");
                return;
            }
            if (password.length() < 6) {
                passwordInput.setError("Min 6 characters");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setEnabled(false);

            loginBtn.postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                loginBtn.setEnabled(true);
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }, 1200);
        });
    }
}