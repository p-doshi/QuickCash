package dal.cs.temp.employer;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import dal.cs.temp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployerDashboard extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_employer);
    }
}