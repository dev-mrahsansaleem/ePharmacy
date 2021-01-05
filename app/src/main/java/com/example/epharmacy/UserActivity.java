package com.example.epharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epharmacy.adapter.medicineAdapter;
import com.example.epharmacy.models.medicineModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    Button btnLogout;
    EditText medSearchBox;
    RecyclerView medicineRecyclerView;
    FirebaseFirestore fStore;
    medicineAdapter medicineAdapter;
    List<medicineModel> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btnLogout = findViewById(R.id.btnLogout);

        medSearchBox = findViewById(R.id.medicineSearch);

        medicineRecyclerView = findViewById(R.id.medicineRecyclerView);
        medicineRecyclerView.setHasFixedSize(true);
        medicineRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        fStore = FirebaseFirestore.getInstance();
        mList = new ArrayList<>();

        medicineAdapter = new medicineAdapter(this, mList);

        medicineRecyclerView.setAdapter(medicineAdapter);
        fStore.collection("medicine").get()
                .addOnCompleteListener(task -> {
                    mList.clear();
                    for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                        medicineModel med = new medicineModel(documentSnapshot.getString("medName"), documentSnapshot.getString("medPrice"));
                        mList.add(med);
                    }
                    medicineAdapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

        medSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<medicineModel> tempList = new ArrayList<>();

                for (medicineModel item : mList) {
                    if (item.getName().toLowerCase().contains(s.toString())) {
                        tempList.add(item);
                    }
                }
                medicineAdapter.filteredList(tempList);
            }
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(UserActivity.this, "logout called", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }
}
