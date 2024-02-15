package com.example.csci3130_group_3;

//import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseRoleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_role);

        // Initialize UI elements
        Button chooseEmployerButton = findViewById(R.id.employerButton);
        Button chooseEmployeeButton = findViewById(R.id.employeeButton);
        Button confirmRoleButton = findViewById(R.id.chooseRoleConfirm);

        chooseEmployerButton.setOnClickListener(view -> role = "Employer");

        chooseEmployeeButton.setOnClickListener(view -> role = "Employee");

        confirmRoleButton.setOnClickListener(view -> {
            if (role == null) {
                Toast.makeText(getApplicationContext(), "Please choose a role!", Toast.LENGTH_LONG).show();
            } else {
                move2DashboardWindow(role);
            }
        });

    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        // No action needed in this case
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // No action needed in this case
    }

    protected void move2DashboardWindow(String role) {
        //commented code are for when all the activities are connected
        //Intent dbIntent;

        if (role.equals("Employer")) {
            //dbIntent = new Intent(getBaseContext(), EmployerDashboard.class);
            Toast.makeText(getApplicationContext(), "Switch to Employer Dashboard", Toast.LENGTH_LONG).show();
        } else {
            //dbIntent = new Intent(getBaseContext(), EmployeeDashboard.class);
            Toast.makeText(getApplicationContext(), "Switch to Employee Dashboard", Toast.LENGTH_LONG).show();

        }

        //startActivity(dbIntent);
    }
}
