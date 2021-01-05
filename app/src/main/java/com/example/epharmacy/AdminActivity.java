package com.example.epharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button btnLogout = findViewById(R.id.logout);

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }
}
