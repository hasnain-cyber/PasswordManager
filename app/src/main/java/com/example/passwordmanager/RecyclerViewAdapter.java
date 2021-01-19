package com.example.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<String> arrayList;
    int r = 30;
    int g = 225;
    int b = 30;
    int shader = 5;

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
        holder.cardView.setBackgroundColor(Color.rgb(r, g, b));
        r += shader;
        g += shader;
        b += shader;
        if (g > 235) {
            r = 30;
            g = 225;
            b = 30;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Random random = new Random();
        private TextView passwordData;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            passwordData = itemView.findViewById(R.id.passwordData);
            cardView = itemView.findViewById(R.id.cardview);
        }

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(context)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Are you sure ?")
                    .setMessage("Do you really want to delete this entry ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        int position = this.getAdapterPosition();
                        String[] strings = arrayList.get(position).split("\n");
                        arrayList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Entry deleted!", Toast.LENGTH_SHORT).show();
                        new PasswordDatabaseHelper(context).deleteData(strings[0], strings[1], strings[2]);
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

}
