package com.example.epharmacy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    TextInputLayout txtFirstName, txtLastName, txtEmail, txtUserName, txtPassword;
    RadioGroup rgUserType;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtRegEmail);
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);

        rgUserType = findViewById(R.id.rgUserType);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {

//        validate

            if (isFieldValid(txtFirstName) && isFieldValid(txtLastName) &&
                    isFieldValid(txtEmail) && isFieldValid(txtUserName) &&
                    isFieldValid(txtPassword)) {
//                Toast.makeText(this, "" + userType(rgUserType.getCheckedRadioButtonId()), Toast.LENGTH_SHORT).show();

                fAuth.createUserWithEmailAndPassword(
                        txtEmail.getEditText().getText().toString(),
                        txtPassword.getEditText().getText().toString())
                        .addOnSuccessListener(authResult -> {
                            Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show();


                            DocumentReference df = fStore.collection("Users")
                                    .document(authResult.getUser().getUid());

                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("firstName", txtFirstName.getEditText().getText().toString());
                            userInfo.put("lastName", txtLastName.getEditText().getText().toString());
                            userInfo.put("email", txtEmail.getEditText().getText().toString());
                            userInfo.put("userName", txtUserName.getEditText().getText().toString());
                            int userType = userType(rgUserType.getCheckedRadioButtonId());
                            userInfo.put("userType", userType);

                            //additional fields
                            if (userType == 2) {
                            // pharmacist
                                userInfo.put("isAuthorized",0);
                            }

                            df.set(userInfo)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(RegisterActivity.this, "Data Stored", Toast.LENGTH_SHORT).show());

                            userNavigator(authResult.getUser().getUid());
                        })
                        .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

            } else {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void userNavigator(String uid) {

        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(documentSnapshot -> {

            switch (Objects.requireNonNull(documentSnapshot.getString("userType"))) {
                case "1":
                    //user
                    startActivity(new Intent(getApplicationContext(), UserActivity.class));
                    break;
                case "2":
                    //pharmacist
                    startActivity(new Intent(getApplicationContext(), PharmacistActivity.class));
                    break;
                case "3":
                    //admin
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    break;
            }
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private boolean isFieldValid(TextInputLayout txt) {
        boolean valid = false;
        if (txt.getEditText().getText().toString().isEmpty()) {
            txt.setError("Empty Field");
        } else {
            valid = true;
            txt.setError("");
        }
        return valid;
    }

    private int userType(int id) {
        if (id == R.id.rbUser) {
            return 1;
        } else if (id == R.id.rbPharmacist) {
            return 2;
        } else if (id == R.id.rbAdmin) {
            return 3;
        }
        return -1;
    }
}
