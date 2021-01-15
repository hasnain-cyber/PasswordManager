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

import java.util.ArrayList;

public class PasswordActivity extends AppCompatActivity {
    PasswordDatabaseHelper myDb;
    EditText editOrganisation, editUsername, editPassword;
    ListView listview;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter adapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new PasswordDatabaseHelper(this);
        cursor = myDb.getCursor();

        while (cursor.moveToNext())
            list.add(cursor.getString(0) + '\n' + cursor.getString(1) + '\n' + cursor.getString(2));

        editOrganisation = findViewById(R.id.enterOrganisation);
        editUsername = findViewById(R.id.enterUsername);
        editPassword = findViewById(R.id.enterPassword);
        listview = findViewById(R.id.listView);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        listview.setVisibility(View.INVISIBLE);

        listview.setOnItemLongClickListener((parent, view, position, id) -> {
            String string = ((TextView) view).getText().toString();
            String[] strings = string.split("\n");
            new AlertDialog.Builder(PasswordActivity.this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Are you sure ?")
                    .setMessage("Do you really want to delete this item ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Toast.makeText(PasswordActivity.this, "Entry deleted!", Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        myDb.deleteData(strings[0], strings[1], strings[2]);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

    }

    public void changeListVisibility(View view) {
        if (listview.getVisibility() == View.VISIBLE)
            listview.setVisibility(View.INVISIBLE);
        else if (listview.getVisibility() == View.INVISIBLE)
            listview.setVisibility(View.VISIBLE);
    }


    public void addData(View view) {
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
                adapter.notifyDataSetChanged();
            } else
                Toast.makeText(PasswordActivity.this, "Entry already exists!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(PasswordActivity.this, "Organisation and password are required fields!", Toast.LENGTH_SHORT).show();
    }
}