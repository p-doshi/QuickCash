package com.example.csci3130_group_3;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployeeDashboard extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_employee);


        BottomNavigationView employeeNavView = findViewById(R.id.employeeBottomNavView);

    }


}
