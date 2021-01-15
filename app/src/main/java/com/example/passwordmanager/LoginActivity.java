package com.example.passwordmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button loginbutton;
    private Button changePasswordButton;
    private EditText masterpass;
    private boolean masterpassexists;
    private String newPass = "";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView fingertext = findViewById(R.id.fingerprinttext);
        masterpass = findViewById(R.id.masterpass);
        loginbutton = findViewById(R.id.loginbutton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        sharedPreferences = getSharedPreferences("password", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initialSetup();

        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        if (fingerprintManager.isHardwareDetected()) {
            if (fingerprintManager.hasEnrolledFingerprints()) {
                fingertext.setText(R.string.scanfingerprint);
                FingerprintManager.AuthenticationCallback authenticationCallback = new FingerprintManager.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                        super.onAuthenticationHelp(helpCode, helpString);
                        Toast.makeText(LoginActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        login(null);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(LoginActivity.this, "Not recognised!", Toast.LENGTH_SHORT).show();
                    }
                };
                fingerprintManager.authenticate(null, null, 0, authenticationCallback, null);
            } else
                fingertext.setText(R.string.nofingerprintenrolled);
        } else {
            fingertext.setText(R.string.fingerprintscannernotdetected);
        }
    }

    private void initialSetup() {
        if (!sharedPreferences.contains("password")) {
            changePasswordButton.setVisibility(View.INVISIBLE);
            masterpass.setHint(R.string.setNewPassword);
            masterpassexists = false;
            loginbutton.setText(R.string.save);
        } else {
            masterpass.setHint(R.string.entermasterpassword);
            masterpassexists = true;
            loginbutton.setText(R.string.verify);
        }
    }

    public void login(View view) {
        Intent intent = new Intent(this, PasswordActivity.class);
        startActivity(intent);
        this.finish(); // destroy login activity
    }

    public void loginButton(View view) {
        String pass = masterpass.getText().toString();
        if (pass.equals("")) {
            if (masterpassexists)
                Toast.makeText(LoginActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(LoginActivity.this, "Please set a password!", Toast.LENGTH_SHORT).show();
        } else {
            if (masterpassexists) {
                if (pass.equals(sharedPreferences.getString("password", null)))
                    login(null);
                else {
                    Toast.makeText(LoginActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                    masterpass.setText("");
                }
            } else {
                if (newPass.equals("")) {
                    newPass = pass;
                    Toast.makeText(LoginActivity.this, "Please confirm password!", Toast.LENGTH_SHORT).show();
                    masterpass.setText("");
                } else {
                    if (newPass.equals(pass)) {
                        editor.putString("password", pass);
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "Password configured!", Toast.LENGTH_SHORT).show();
                        login(null);
                    } else {
                        Toast.makeText(LoginActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                        masterpass.setText("");
                        newPass = "";
                    }
                }
            }
        }
    }

    public void setChangePasswordButton(View view) {
        String oldPass = masterpass.getText().toString();
        if (!(oldPass.equals(""))) {
            if (oldPass.equals(sharedPreferences.getString("password", "-1"))) {
                masterpassexists = false;
                editor.remove("password");
                editor.apply();
                masterpass.setText("");
                Toast.makeText(LoginActivity.this, "Now enter new password!", Toast.LENGTH_SHORT).show();
                initialSetup();
            } else
                Toast.makeText(LoginActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(LoginActivity.this, "Please enter old password!", Toast.LENGTH_SHORT).show();
    }
}