package dal.cs.quickcash3.worker;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.annotations.Nullable;

import java.util.Set;
import java.util.TreeSet;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.jobdetail.ApplyJob;
import dal.cs.quickcash3.jobdetail.BackButtonListener;
import dal.cs.quickcash3.jobdetail.JobDetailsPage;
import dal.cs.quickcash3.jobs.JobSearchFragment;
import dal.cs.quickcash3.fragments.MapsFragment;
import dal.cs.quickcash3.fragments.ProfileFragment;
import dal.cs.quickcash3.fragments.ReceiptsFragment;
import dal.cs.quickcash3.location.AndroidLocationProvider;
import dal.cs.quickcash3.location.LocationProvider;
import dal.cs.quickcash3.location.MockLocationProvider;
import dal.cs.quickcash3.permission.AppCompatPermissionActivity;

public class WorkerDashboard extends AppCompatPermissionActivity {
    private static final String LOG_TAG = WorkerDashboard.class.getSimpleName();
    private Database database;
    private LocationProvider locationProvider;
    private Fragment jobSearchFragment;

    @SuppressWarnings("PMD.LawOfDemeter") // There is no other way to do this.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_worker);

        initInterfaces();

        // Initialize the fragments.
        Fragment receiptsFragment = new ReceiptsFragment();
        Fragment mapFragment = new MapsFragment();
        Fragment profileFragment = new ProfileFragment();
        jobSearchFragment = new JobSearchFragment(this, database, locationProvider,this::switchToJobDetails);

        BottomNavigationView workerNavView = findViewById(R.id.workerBottomNavView);

        workerNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.workerHistoryPage) {
                replaceFragment(receiptsFragment);
                return true;
            }
            else if (itemId == R.id.workerSearchPage) {
                replaceFragment(jobSearchFragment);
                return true;
            }
            else if (itemId == R.id.workerMapPage) {
                replaceFragment(mapFragment);
                return true;
            }
            else if (itemId == R.id.workerProfilePage) {
                replaceFragment(profileFragment);
                return true;
            }
            else {
                throw new IllegalArgumentException("Unrecognized item ID: " + itemId);
            }
        });

        workerNavView.setSelectedItemId(R.id.workerMapPage);
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        Log.v(LOG_TAG, "Showing " + fragment.getClass().getSimpleName());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.workerFragmentView, fragment);
        transaction.commit();
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void switchToJobDetails(@NonNull AvailableJob availableJob) {
        Fragment applyFragment = new ApplyJob();
        Fragment jobDetailsPage = new JobDetailsPage(availableJob, applyFragment);
        replaceFragment(jobDetailsPage);
        getOnBackPressedDispatcher().addCallback(jobDetailsPage,
            new BackButtonListener(() -> replaceFragment(jobSearchFragment)));
    }

    private void initInterfaces() {
        Set<String> categories = getIntent().getCategories();
        if (categories == null) {
            categories = new TreeSet<>();
        }

        if (categories.contains(getString(R.string.MOCK_DATABASE))) {
            database = new MockDatabase();
            Log.d(LOG_TAG, "Using Mock Database");
        }
        else {
            database = new MyFirebaseDatabase();
        }

        if (categories.contains(getString(R.string.MOCK_LOCATION))) {
            locationProvider = new MockLocationProvider();
            Log.d(LOG_TAG, "Using Mock Location Provider");
        }
        else {
            locationProvider = new AndroidLocationProvider(this, 5000); // Update location every 5 seconds.
        }
    }

    public @NonNull Database getDatabase() {
        return database;
    }

    public @NonNull LocationProvider getLocationProvider() {
        return locationProvider;
    }
}