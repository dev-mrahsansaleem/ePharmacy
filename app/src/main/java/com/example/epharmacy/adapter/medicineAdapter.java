package com.example.epharmacy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epharmacy.R;
import com.example.epharmacy.models.medicineModel;

import java.util.ArrayList;
import java.util.List;

public class medicineAdapter extends RecyclerView.Adapter<medicineAdapter.medicineViewHolder> {
    private Context context;
    private List<medicineModel> mList;

    public medicineAdapter(Context context, List<medicineModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public medicineAdapter.medicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.medicine_rv_item, parent, false);

        return new medicineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull medicineAdapter.medicineViewHolder holder, int position) {
        holder.txtNameField.setText(mList.get(position).getName());
        holder.txtPriceField.setText(mList.get(position).getPrice());
        holder.btnView.setOnClickListener(v -> Toast.makeText(context, ""+ mList.get(position).getName(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void filteredList(ArrayList<medicineModel> tempList) {
        mList = tempList;
        notifyDataSetChanged();
    }

    static class medicineViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNameField, txtPriceField;
        private Button btnView;


        medicineViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameField = itemView.findViewById(R.id.medicineName);
            txtPriceField = itemView.findViewById(R.id.medicinePrice);
            btnView = itemView.findViewById(R.id.btnMedicineView);
        }
    }
}
