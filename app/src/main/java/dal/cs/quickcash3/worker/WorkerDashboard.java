package dal.cs.quickcash3.worker;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.annotations.Nullable;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.jobdetail.ApplyJob;
import dal.cs.quickcash3.payment.WorkerCheckPayment;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.jobdetail.JobDetailsPage;
import dal.cs.quickcash3.jobs.JobSearchFragment;
import dal.cs.quickcash3.fragments.MapsFragment;
import dal.cs.quickcash3.fragments.ProfileFragment;
import dal.cs.quickcash3.fragments.HistoryFragment;
import dal.cs.quickcash3.location.AndroidLocationProvider;
import dal.cs.quickcash3.location.LocationProvider;
import dal.cs.quickcash3.location.MockLocationProvider;
import dal.cs.quickcash3.permission.AppCompatPermissionActivity;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.util.BackButtonListener;

public class WorkerDashboard extends AppCompatPermissionActivity {
    private static final String LOG_TAG = WorkerDashboard.class.getSimpleName();
    private Database database;
    private LocationProvider locationProvider;
    private Fragment historyFragment;
    private Fragment jobSearchFragment;

    @SuppressWarnings("PMD.LawOfDemeter") // There is no other way to do this.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_worker);

        initInterfaces();

        // Get a search filter for the current user.
        String userId = getIntent().getStringExtra(getString(R.string.USER));
        RegexSearchFilter<CompletedJob> searchFilter = new RegexSearchFilter<>(CompletedJob::getWorker);
        if (userId == null) {
            searchFilter.setPattern(Pattern.compile(".*"));
        }
        else {
            searchFilter.setPattern(Pattern.compile(userId));
        }

        // Get a search filter for the current user.
        String userId = getIntent().getStringExtra(getString(R.string.USER));
        RegexSearchFilter<CompletedJob> searchFilter = new RegexSearchFilter<>(CompletedJob::getWorker);
        if (userId == null) {
            searchFilter.setPattern(Pattern.compile(".*"));
        }
        else {
            searchFilter.setPattern(Pattern.compile(userId));
        }

        // Initialize the fragments.
        historyFragment = new HistoryFragment(this, database, searchFilter, this::switchToJobHistory);
        Fragment mapFragment = new MapsFragment();
        Fragment profileFragment = new ProfileFragment();
        jobSearchFragment = new JobSearchFragment(this, database, locationProvider,this::switchToJobDetails);
        Fragment statsFragment = new WorkHistoryGraphFragment(database);

        BottomNavigationView workerNavView = findViewById(R.id.workerBottomNavView);

        workerNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.workerHistoryPage) {
                replaceFragment(historyFragment);
            if (itemId == R.id.workerHistoryPage) {
                replaceFragment(historyFragment);
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
            } else if (itemId == R.id.workerStatsPage) {
                replaceFragment(statsFragment);
                return true;
            } else {
            } else if (itemId == R.id.workerStatsPage) {
                replaceFragment(statsFragment);
                return true;
            } else {
                throw new IllegalArgumentException("Unrecognized item ID: " + itemId);
            }
        });

        workerNavView.setSelectedItemId(R.id.workerMapPage);
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        Log.i(LOG_TAG, "Showing " + fragment.getClass().getSimpleName());
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.workerFragmentView, fragment);
        transaction.commit();
    }

    private void addFragment(@NonNull Fragment fragment) {
        Log.i(LOG_TAG, "Showing " + fragment.getClass().getSimpleName());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.workerFragmentView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void switchToJobDetails(@NonNull AvailableJob job) {
        Fragment applyFragment = new ApplyJob();
        Fragment jobDetailsPage = new JobDetailsPage(job, applyFragment);
        addFragment(jobDetailsPage);
    }

    private void switchToJobHistory(@NonNull CompletedJob job) {
        Fragment workerCheckPayment = new WorkerCheckPayment(database);
        Fragment jobDetailsPage = new JobDetailsPage(job, workerCheckPayment);
        addFragment(jobDetailsPage);
    }

    private void initInterfaces() {
        Set<String> categories = getIntent().getCategories();
        if (categories == null) {
            categories = new TreeSet<>();
        }

        if (categories.contains(getString(R.string.MOCK_DATABASE))) {
            database = new MockDatabase();
            Log.i(LOG_TAG, "Using Mock Database");
            Log.i(LOG_TAG, "Using Mock Database");
        }
        else {
            database = new MyFirebaseDatabase();
        }

        if (categories.contains(getString(R.string.MOCK_LOCATION))) {
            locationProvider = new MockLocationProvider();
            Log.i(LOG_TAG, "Using Mock Location Provider");
            Log.i(LOG_TAG, "Using Mock Location Provider");
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
