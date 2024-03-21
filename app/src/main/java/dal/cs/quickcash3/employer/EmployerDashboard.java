package dal.cs.quickcash3.employer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import dal.cs.quickcash3.R;

public class EmployerDashboard extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_employer);
        this.setUpAddJobButton();
    }

    /**
     * handle on click of 'add job' button
     */
    protected void setUpAddJobButton(){
        Button addJobButton = findViewById(R.id.addJobButton);
        addJobButton.setOnClickListener(view -> {
            //move to page
        });
    }
}