package com.example.csci3130_group_3;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseRoleActivity extends AppCompatActivity {
    private String role;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_role);

        // Initialize UI elements
        Button chooseEmployerButton = findViewById(R.id.employerButton);
        Button chooseEmployeeButton = findViewById(R.id.employeeButton);
        Button confirmRoleButton = findViewById(R.id.chooseRoleConfirm);

        chooseEmployerButton.setOnClickListener(view -> role = getResources().getString(R.string.employer));

        chooseEmployeeButton.setOnClickListener(view -> role = getResources().getString(R.string.employee));

        confirmRoleButton.setOnClickListener(view -> {
            if (role == null) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.choose_role_reminder), Toast.LENGTH_LONG).show();
            } else {
                moveToDashboardWindow(role);
            }
        });

    }

    protected void moveToDashboardWindow(@NonNull String role) {
        if (role.equals(getResources().getString(R.string.employer))) {
            Toast.makeText(getApplicationContext(), "Switch to Employer Dashboard", Toast.LENGTH_LONG).show();
        } else if (role.equals(getResources().getString(R.string.employee))) {
            Toast.makeText(getApplicationContext(), "Switch to Employee Dashboard", Toast.LENGTH_LONG).show();
        } else {
            Log.wtf(getResources().getString(R.string.choose_role), getResources().getString(R.string.error_choose_role));
            System.exit(1);
        }
    }
}
