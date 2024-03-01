package com.example.csci3130_group_3.employer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.csci3130_group_3.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployerDashboard extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_employer);

        BottomNavigationView employerNavView = findViewById(R.id.employerBottomNavView);

    }


}