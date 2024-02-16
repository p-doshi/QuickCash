package com.example.csci3130_group_3;

//import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseRoleActivity extends AppCompatActivity {
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                Toast.makeText(getApplicationContext(), "Please choose a role!", Toast.LENGTH_LONG).show();
            } else {
                move2DashboardWindow(role);
            }
        });

    }

    protected void move2DashboardWindow(String role) {
        //commented code are for when all the activities are connected
        //Intent dashboardIntent;

        if (role.equals("Employer")) {
            //dashboardIntent = new Intent(getBaseContext(), EmployerDashboard.class);
            Toast.makeText(getApplicationContext(), "Switch to Employer Dashboard", Toast.LENGTH_LONG).show();
        } else if (role.equals("Employee")) {
            //dashboardIntent = new Intent(getBaseContext(), EmployeeDashboard.class);
            Toast.makeText(getApplicationContext(), "Switch to Employee Dashboard", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
        }

        //startActivity(dashboardIntent);
    }
}
