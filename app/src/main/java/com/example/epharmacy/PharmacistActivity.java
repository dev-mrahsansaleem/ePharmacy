package com.example.epharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PharmacistActivity extends AppCompatActivity {

    Button btnAddMed, btnLogout;
    TextInputLayout txtMedName, txtMedPrice;

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Pharmacist");

        btnAddMed = findViewById(R.id.btnAddMed);
        btnLogout = findViewById(R.id.btnLogout);

        txtMedName = findViewById(R.id.txtMedName);
        txtMedPrice = findViewById(R.id.txtMedPrice);

        fStore = FirebaseFirestore.getInstance();

        btnAddMed.setOnClickListener(v -> {
            Toast.makeText(PharmacistActivity.this, "med add call", Toast.LENGTH_SHORT).show();

            if (isFieldValid(txtMedName) && isFieldValid(txtMedPrice)) {

                String id = UUID.randomUUID().toString();
                Map<String, Object> data = new HashMap<>();
                data.put("medName", Objects.requireNonNull(txtMedName.getEditText()).getText().toString());
                data.put("medPrice", Objects.requireNonNull(txtMedPrice.getEditText()).getText().toString());
                fStore.collection("medicine").document(id).set(data)
                        .addOnCompleteListener(task -> {
                            Toast.makeText(this, "Med added success", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            Toast.makeText(this, "logout called", Toast.LENGTH_SHORT).show();
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

}
