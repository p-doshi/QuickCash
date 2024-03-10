package dal.cs.temp.worker;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import dal.cs.temp.R;

public class WorkerDashboard extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_worker);
    }
}
