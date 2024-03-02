package dal.cs.quickCash3.worker;

import android.app.Activity;
import android.os.Bundle;

import dal.cs.quickCash3.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployeeDashboard extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_employee);


        BottomNavigationView employeeNavView = findViewById(R.id.employeeBottomNavView);

    }


}
