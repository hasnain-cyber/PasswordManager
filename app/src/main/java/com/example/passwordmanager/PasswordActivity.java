package com.example.passwordmanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwordmanager.adapter.RecyclerViewAdapter;

import java.util.ArrayList;

public class PasswordActivity extends AppCompatActivity {

    private PasswordDatabaseHelper myDb;
    private EditText editOrganisation, editUsername, editPassword;
    private Cursor cursor;
    private ArrayList<String> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        myDb = new PasswordDatabaseHelper(this);
        cursor = myDb.getCursor();

        while (cursor.moveToNext())
            list.add(cursor.getString(0) + '\n' + cursor.getString(1) + '\n' + cursor.getString(2));

        editOrganisation = findViewById(R.id.enterOrganisation);
        editUsername = findViewById(R.id.enterUsername);
        editPassword = findViewById(R.id.enterPassword);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(PasswordActivity.this, list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PasswordActivity.this));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setVisibility(View.INVISIBLE);

    }

    public void changeListVisibility(View view) {
        if (recyclerView.getVisibility() == View.VISIBLE)
            recyclerView.setVisibility(View.INVISIBLE);
        else if (recyclerView.getVisibility() == View.INVISIBLE)
            recyclerView.setVisibility(View.VISIBLE);
    }


    public void setSaveButton(View view) {
        cursor = myDb.getCursor();
        String t1 = editOrganisation.getText().toString().trim();
        String t2 = editUsername.getText().toString().trim();
        String t3 = editPassword.getText().toString().trim();
        boolean flag = true;
        if (!t1.equals("") && !t3.equals("")) {
            while (cursor.moveToNext())
                if (cursor.getString(0).equals(t1) && cursor.getString(1).equals(t2) && cursor.getString(2).equals(t3))
                    flag = false;
            if (flag) {
                boolean isInserted = myDb.insertData(t1, t2, t3);
                if (isInserted) {
                    editOrganisation.setText("");
                    editUsername.setText("");
                    editPassword.setText("");
                    Toast.makeText(PasswordActivity.this, "Entry inserted", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(PasswordActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                list.add(t1 + "\n" + t2 + "\n" + t3);
                recyclerViewAdapter.notifyDataSetChanged();
            } else
                Toast.makeText(PasswordActivity.this, "Entry already exists!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(PasswordActivity.this, "Organisation and password are required fields!", Toast.LENGTH_SHORT).show();
    }
}