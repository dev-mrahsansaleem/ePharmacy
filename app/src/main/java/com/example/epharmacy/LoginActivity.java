package com.example.epharmacy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout txtEmail, txtPassword;
    Button btnLogin, btnCreateAccount;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);  // added for top bar arrow

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);


        btnLogin = findViewById(R.id.btnLogin);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnLogin.setOnClickListener(v -> {
            if (isFieldValid(txtEmail) && isFieldValid(txtPassword)) {
                fAuth.signInWithEmailAndPassword(
                        txtEmail.getEditText().getText().toString(),
                        txtPassword.getEditText().getText().toString())
                        .addOnSuccessListener(authResult -> {
                            userNavigator(authResult.getUser().getUid());
                        }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

            } else {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            }
        });
        btnCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });


    }

    private void userNavigator(String uid) {

        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(documentSnapshot -> {

            if (Objects.requireNonNull(documentSnapshot.get("userType")).toString().equals("1")) {
                //user
                startActivity(new Intent(getApplicationContext(), UserActivity.class));
                finish();
            } else if (Objects.requireNonNull(documentSnapshot.get("userType")).toString().equals("2")) {
                //pharmacist
                startActivity(new Intent(getApplicationContext(), PharmacistActivity.class));
                finish();
            } else if (Objects.requireNonNull(documentSnapshot.get("userType")).toString().equals("3")) {
                //admin
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                finish();
            }
            Toast.makeText(this, Objects.requireNonNull(documentSnapshot.get("userType")).toString(), Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private boolean isFieldValid(TextInputLayout txt) {
        boolean valid = false;
        if (Objects.requireNonNull(txt.getEditText()).getText().toString().isEmpty()) {
            txt.setError("Empty Field");
        } else {
            valid = true;
            txt.setError("");
        }
        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userNavigator(FirebaseAuth.getInstance().getUid());
        }
    }
}
