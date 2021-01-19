package com.example.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private final Context context;
    private final ArrayList<String> arrayList;

    public RecyclerViewAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.passwordData.setText(arrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView passwordData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            passwordData = itemView.findViewById(R.id.passwordData);
        }

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(context)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Are you sure ?")
                    .setMessage("Do you really want to delete this entry ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        int position = this.getAdapterPosition();
                        String[]strings = arrayList.get(position).split("\n");
                        arrayList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Entry deleted!", Toast.LENGTH_SHORT).show();
                        new PasswordDatabaseHelper(context).deleteData(strings[0],strings[1],strings[2]);
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

}
