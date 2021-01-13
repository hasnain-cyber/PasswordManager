package com.example.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);
    }

    public void setPassImage(View view) {
        Intent intent = new Intent(this, PasswordActivity.class);
        startActivity(intent);
        this.finish();
    }
}