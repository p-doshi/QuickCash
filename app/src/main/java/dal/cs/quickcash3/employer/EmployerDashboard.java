package dal.cs.quickcash3.employer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import dal.cs.quickcash3.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployerDashboard extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_employer);

        BottomNavigationView employerNavView = findViewById(R.id.employerBottomNavView);

    }


}