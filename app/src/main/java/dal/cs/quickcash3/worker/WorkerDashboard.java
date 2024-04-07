package dal.cs.quickcash3.worker;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.annotations.Nullable;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.data.JobPost;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.jobdetail.ApplyJob;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.util.BackButtonListener;
import dal.cs.quickcash3.jobdetail.JobDetailsPage;
import dal.cs.quickcash3.jobs.JobSearchFragment;
import dal.cs.quickcash3.fragments.MapFragment;
import dal.cs.quickcash3.fragments.ProfileFragment;
import dal.cs.quickcash3.fragments.HistoryFragment;
import dal.cs.quickcash3.location.AndroidLocationProvider;
import dal.cs.quickcash3.location.LocationProvider;
import dal.cs.quickcash3.location.MockLocationProvider;
import dal.cs.quickcash3.permission.AppCompatPermissionActivity;

public class WorkerDashboard extends AppCompatPermissionActivity {
    private static final String LOG_TAG = WorkerDashboard.class.getSimpleName();
    private Database database;
    private LocationProvider locationProvider;
    private MapFragment mapFragment;
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

        // Initialize the fragments.
        Fragment historyFragment = new HistoryFragment(this, database, searchFilter, this::switchToJobDetails);
        mapFragment = new MapFragment();
        Fragment profileFragment = new ProfileFragment();
        jobSearchFragment = new JobSearchFragment(this, database, locationProvider,this::switchToJobDetails);

        BottomNavigationView workerNavView = findViewById(R.id.workerBottomNavView);

        workerNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.workerHistoryPage) {
                replaceFragment(historyFragment);
                return true;
            }
            else if (itemId == R.id.workerSearchPage) {
                replaceFragment(jobSearchFragment);
                return true;
            }
            else if (itemId == R.id.workerMapPage) {
                Log.v(LOG_TAG, "Showing map fragment");
                locationProvider.fetchLocation(this::showLocation, this::showLocationError);

                // Test account parthdoshi135@gmail.com : Password
                // Here we would grab all job locations from database, then add to map
                //while (job!=null) {
                //  mapFragment.addJob("JobName", LatLong position); }

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

        // TODO: This might have issues with sync
        workerNavView.setSelectedItemId(R.id.workerReceiptPage);
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        Log.i(LOG_TAG, "Showing " + fragment.getClass().getSimpleName());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.workerFragmentView, fragment);
        transaction.commit();
    }

    private void switchToJobDetails(@NonNull JobPost job) {
        Fragment applyFragment = new ApplyJob();
        Fragment jobDetailsPage = new JobDetailsPage(job, applyFragment);
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
            Log.i(LOG_TAG, "Using Mock Database");
        }
        else {
            database = new MyFirebaseDatabase();
        }

        if (categories.contains(getString(R.string.MOCK_LOCATION))) {
            locationProvider = new MockLocationProvider();
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

    private void showLocation(@NonNull LatLng location) {
        LatLng latlng = new LatLng(location.latitude, location.longitude);
        mapFragment.setCurrentLocation(latlng);
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void showLocationError(@NonNull String error) {
        Log.w(LOG_TAG, error);
    }
}