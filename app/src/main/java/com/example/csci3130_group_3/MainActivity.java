package com.example.csci3130_group_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    EditText emailInput;
    TextInputEditText passwordInput;
    Toast toastMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the inputs.
        emailInput = findViewById(R.id.emailaddress);
        passwordInput = findViewById(R.id.etPassword);

        // Create a toast message.
        toastMsg = Toast.makeText(this, getResources().getString(R.string.TOAST_STRING), Toast.LENGTH_SHORT);

        // Set the button functionality.
        Button continueButton = findViewById(R.id.Continue);
        continueButton.setOnClickListener(v -> {
            validateAndLogin();
        });
    }

    void validateAndLogin() {
        // Get the text inputs here.

        // Email and password validation here.

        // If we haven't returned yet, then valid credentials.
        toastMsg.show();
    }
}