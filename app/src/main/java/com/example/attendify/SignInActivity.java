package com.example.attendify;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button btnLogIn;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in); // Ensure this matches your XML file name

        // Initialize UI elements
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogIn = findViewById(R.id.btnLogIn);

        firebaseAuth = FirebaseAuth.getInstance();

        btnLogIn.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Validate email
            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError("Email is required!");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Enter a valid email address!");
                return;
            }

            // Validate password
            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Password is required!");
                return;
            }
            if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")) { // Minimum 6 characters, at least 1 letter and 1 number
                editTextPassword.setError("Password must contain at least 6 characters, including a letter and a number!");
                return;
            }

            // Authenticate with Firebase
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Sign In Successful!", Toast.LENGTH_SHORT).show();
                            // Redirect to main activity or another screen
                        } else {
                            Toast.makeText(SignInActivity.this, "Sign In Failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}