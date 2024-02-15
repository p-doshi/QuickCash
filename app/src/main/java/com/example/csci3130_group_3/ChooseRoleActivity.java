package com.example.csci3130_group_3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseRoleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String role;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_role);
        database = new MyFirebaseDatabase(this);

        // Initialize UI elements
        Button chooseEmployerButton = findViewById(R.id.employerButton);
        Button chooseEmployeeButton = findViewById(R.id.employeeButton);
        Button confirmRoleButton = findViewById(R.id.chooseRoleConfirm);

        chooseEmployerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                role = "Employer";
            }
        });

        chooseEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                role = "Employee";
            }
        });

        confirmRoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call method to write to database
                if (role == null) {
                    Toast.makeText(getApplicationContext(), "Please choose a role!", Toast.LENGTH_LONG).show();
                } else {
                    saveInfoToFirebase(role);
                    move2DashboardWindow(role);
                }
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


    // put info to database
    protected void saveInfoToFirebase(String role) {
        database.write("role", role);
    }


    protected void move2DashboardWindow(String role) {
        Intent dbIntent;

        if (role.equals("Employer")) {
            dbIntent = new Intent(getBaseContext(), EmployerDashboard.class);
        } else {
            dbIntent = new Intent(getBaseContext(), EmployeeDashboard.class);
        }

        startActivity(dbIntent);
    }
}
