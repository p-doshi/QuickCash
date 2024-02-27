package com.example.csci3130_group_3;

import android.os.Bundle;
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

        chooseEmployerButton.setOnClickListener(view -> role = getString(R.string.employer));

        chooseEmployeeButton.setOnClickListener(view -> role = getString(R.string.employee));

        confirmRoleButton.setOnClickListener(view -> {
            if (role == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.choose_role_reminder), Toast.LENGTH_LONG).show();
            } else {
                moveToDashboardWindow(role);
            }
        });

    }

    protected void moveToDashboardWindow(@NonNull String role) {
        if (role.equals(getString(R.string.employer))) {
            Toast.makeText(getApplicationContext(), "Switch to Employer Dashboard", Toast.LENGTH_LONG).show();
        } else if (role.equals(getString(R.string.employee))) {
            Toast.makeText(getApplicationContext(), "Switch to Employee Dashboard", Toast.LENGTH_LONG).show();
        } else {
            throw new IllegalArgumentException("A deadly error has occur when user is choosing role");
        }
    }
}
