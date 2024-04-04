package dal.cs.quickcash3.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.employer.EmployerDashboard;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class MoveToDashboard  {

    private final Context context;

    public MoveToDashboard(@NonNull Context context){
        this.context=context;
    }

    public void dashBoardManager(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Database database = new MyFirebaseDatabase();

        if (user != null) {
            String location = "private/users/" + user.getUid() + "/role";

            database.read(location, String.class,
                    role -> {
                        if (role != null) {
                            launchDashboard(role);
                        } else {
                            Log.e("DataError", "User role not found in database "+user.getUid());
                        }
                    },
                    error -> Log.e("DataError", "Error reading user role from database: " + error)
            );
        } else {
            Log.e("AuthError", "User is not authenticated");
        }
    }


    private void launchDashboard(String role) {
        Intent dashboardIntent = null;
        if (role.equals(this.context.getResources().getString(R.string.employer))) {
            dashboardIntent = new Intent(this.context, EmployerDashboard.class);
        } else if (role.equals(context.getResources().getString(R.string.worker))) {
            dashboardIntent = new Intent(context, WorkerDashboard.class);
        } else {
            Log.wtf(context.getResources().getString(R.string.choose_role_reminder), context.getResources().getString(R.string.choose_role_reminder));
            System.exit(1);
        }
        context.startActivity(dashboardIntent);    }
}
