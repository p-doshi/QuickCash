package dal.cs.quickcash3.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.employer.EmployerDashboard;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class ChooseRoleActivity extends AppCompatActivity {
    private String role;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_role);

        // Initialize UI elements
        Button chooseEmployerButton = findViewById(R.id.employerButton);
        Button chooseWorkerButton = findViewById(R.id.workerButton);
        Button confirmRoleButton = findViewById(R.id.chooseRoleConfirm);

        chooseEmployerButton.setOnClickListener(view -> role = getString(R.string.employer));

        chooseWorkerButton.setOnClickListener(view -> role = getString(R.string.worker));

        confirmRoleButton.setOnClickListener(view -> {
            if (role == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.choose_role_reminder), Toast.LENGTH_LONG).show();
            } else {
                moveToDashboardWindow(role);
            }
        });

    }

    private void moveToDashboardWindow(@NonNull String role) {
        Intent dashboardIntent = null;

        if (role.equals(getString(R.string.employer))) {
            dashboardIntent = new Intent(getBaseContext(), EmployerDashboard.class);
        } else if (role.equals(getString(R.string.worker))) {
            dashboardIntent = new Intent(getBaseContext(), WorkerDashboard.class);
        } else {
            throw new IllegalArgumentException("A deadly error has occur when user is choosing role");
        }

        startActivity(dashboardIntent);
    }
}
