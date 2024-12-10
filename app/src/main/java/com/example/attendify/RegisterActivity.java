package com.example.attendify;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference database;

    private EditText etUsername, etEmail, etMobile, etPassword, etConfirmPassword;
    private Button btnLogin, btnSignIn;

    // Updated regex patterns
    private Pattern namePattern = Pattern.compile("^[a-zA-Z.]+$");
    private Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@(gmail|yahoo|outlook)\\.com$");
    private Pattern mobilePattern = Pattern.compile("^(017|018|019)[0-9]{8}$");
    private Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,20}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        etUsername = findViewById(R.id.editTextName);
        etEmail = findViewById(R.id.editTextEmail);
        etMobile = findViewById(R.id.editTextPhoneNumber);
        etPassword = findViewById(R.id.editTextPassword);
        etConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnSignIn = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogIn);

        // Sign Up button logic
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String mobile = etMobile.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (validateInput(username, email, mobile, password, confirmPassword)) {
                    // Register user with Firebase Authentication
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();

                                    // Store additional data in Firebase Realtime Database
                                    User userInfo = new User(username, email, mobile);
                                    database.child("users").child(user.getUid()).setValue(userInfo)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // Login button logic
        btnLogin.setOnClickListener(v -> Toast.makeText(RegisterActivity.this, "Login Clicked", Toast.LENGTH_SHORT).show());
    }

    private boolean validateInput(String username, String email, String mobile, String password, String confirmPassword) {
        if (TextUtils.isEmpty(username) || !namePattern.matcher(username).matches()) {
            etUsername.setError("Username can only contain letters and periods.");
            return false;
        }
        if (TextUtils.isEmpty(email) || !emailPattern.matcher(email).matches()) {
            etEmail.setError("Invalid email format. Use a valid gmail, yahoo, or outlook email.");
            return false;
        }
        if (TextUtils.isEmpty(mobile) || !mobilePattern.matcher(mobile).matches()) {
            etMobile.setError("Mobile number must start with 017, 018, or 019 and be 11 digits long.");
            return false;
        }
        if (TextUtils.isEmpty(password) || !passwordPattern.matcher(password).matches()) {
            etPassword.setError("Password must be 6-20 characters long, contain at least one digit, one uppercase letter, and one lowercase letter.");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match.");
            return false;
        }
        return true;
    }

    // User class to store data in Firebase
    public static class User {
        public String username;
        public String email;
        public String mobile;

        public User(String username, String email, String mobile) {
            this.username = username;
            this.email = email;
            this.mobile = mobile;
        }
    }
}