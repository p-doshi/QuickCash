package dal.cs.quickcash3.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.Account;
import dal.cs.quickcash3.data.UserRole;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.employer.EmployerDashboard;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class DashboardLauncher {
    private static final String LOG_TAG = DashboardLauncher.class.getSimpleName();
    private final Activity activity;
    private final Database database;

    public DashboardLauncher(@NonNull Activity activity, @NonNull Database database) {
        this.activity = activity;
        this.database = database;
    }

    public void launchDashboard(@NonNull String userId) {
        Account.readFromDatabase(database, userId,
            this::launchFromAccount,
            error -> {
                Log.e(LOG_TAG, "Database error: " + error);
                Toast.makeText(activity, "Database Failure", Toast.LENGTH_SHORT).show();
            });
    }

    private void launchFromAccount(@NonNull Account account) {
        Intent dashboardIntent;
        String role = account.getRole();

        if (UserRole.EMPLOYER.getValue().equals(role)) {
            dashboardIntent = new Intent(activity, EmployerDashboard.class);
        }
        else if (UserRole.WORKER.getValue().equals(role)) {
            dashboardIntent = new Intent(activity, WorkerDashboard.class);
        }
        else {
            throw new IllegalArgumentException("Account role not recognized: " + role);
        }

        dashboardIntent.putExtra(activity.getString(R.string.USER), account.getId());

        activity.startActivity(dashboardIntent);

        activity.finish();
    }
}
