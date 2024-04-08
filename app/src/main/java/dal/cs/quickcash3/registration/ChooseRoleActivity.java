package dal.cs.quickcash3.registration;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.Account;
import dal.cs.quickcash3.data.Employer;
import dal.cs.quickcash3.data.RegisteringUser;
import dal.cs.quickcash3.data.User;
import dal.cs.quickcash3.data.UserRole;
import dal.cs.quickcash3.data.Worker;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.location.AndroidLocationProvider;
import dal.cs.quickcash3.location.MockLocationProvider;
import dal.cs.quickcash3.util.DashboardLauncher;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class ChooseRoleActivity extends AppCompatActivity {
    private static final String LOG_TAG = ChooseRoleActivity.class.getSimpleName();
    private DashboardLauncher dashboardLauncher;
    private UserRole role;
    private Database database;
    private RegisteringUser registeringUser;

    @SuppressWarnings({"unchecked", "deprecation"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_role);
        initInterfaces();
        registeringUser = getIntent().getParcelableExtra("User");
        dashboardLauncher = new DashboardLauncher(this, database);

        // Initialize UI elements
        Button chooseEmployerButton = findViewById(R.id.employerButton);
        Button chooseWorkerButton = findViewById(R.id.workerButton);
        Button confirmRoleButton = findViewById(R.id.chooseRoleConfirm);

        chooseEmployerButton.setOnClickListener(view -> role = UserRole.EMPLOYER);

        chooseWorkerButton.setOnClickListener(view -> role = UserRole.WORKER);

        confirmRoleButton.setOnClickListener(view -> {
            if (role == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.choose_role_reminder), Toast.LENGTH_LONG).show();
            } else {
                createAccount();
            }
        });

    }

    private void createAccount() {
        assert registeringUser.getEmail() != null;
        assert registeringUser.getPassword() != null;
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(registeringUser.getEmail(), registeringUser.getPassword())
                .addOnSuccessListener(result -> {
                    String userID = Objects.requireNonNull(result.getUser()).getUid();
                    Account account = Account.create(userID);
                    // convert registeringUser to employer or worker depending on role
                    User user;
                    if (UserRole.EMPLOYER.equals(role)) {
                        user = Employer.create(registeringUser);
                    } else {
                        user = Worker.create(registeringUser);
                    }
                    user.writeToDatabase(database, () -> {
                        account.setRole(role.getValue());
                        account.setId(Objects.requireNonNull(user.key()));
                        account.writeToDatabase(database, () -> {
                            assert account.getId() != null;
                            dashboardLauncher.launchDashboard(account.getId());
                        }, error -> Log.e(LOG_TAG, error));
                    }, error -> Log.e(LOG_TAG, error));
                });
    }

    private void initInterfaces() {
        Set<String> categories = getIntent().getCategories();
        if (categories == null) {
            categories = new TreeSet<>();
        }

        if (categories.contains(getString(R.string.MOCK_DATABASE))) {
            database = new MockDatabase();
            Log.i(LOG_TAG, "Using Mock Database");
        }
        else {
            database = new MyFirebaseDatabase();
        }
    }

    public @NonNull Database getDatabase() {
        return database;
    }
}
