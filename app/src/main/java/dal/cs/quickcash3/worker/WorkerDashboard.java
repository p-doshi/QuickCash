package dal.cs.quickcash3.worker;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dal.cs.quickcash3.R;

public class WorkerDashboard extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_worker);


        BottomNavigationView workerNavView = findViewById(R.id.workerBottomNavView);

    }


}