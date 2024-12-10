package com.example.attendify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.btnLogIn).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, SignInActivity.class))
        );

        findViewById(R.id.btnRegister).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, RegisterActivity.class))
        );
    }
}