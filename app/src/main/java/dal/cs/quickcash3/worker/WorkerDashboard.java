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
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.MockDatabase;
import dal.cs.quickcash3.database.MyFirebaseDatabase;
import dal.cs.quickcash3.fragments.MapsFragment;
import dal.cs.quickcash3.fragments.ProfileFragment;
import dal.cs.quickcash3.fragments.ReceiptsFragment;
import dal.cs.quickcash3.fragments.SearchFragment;
import dal.cs.quickcash3.location.AndroidLocationProvider;
import dal.cs.quickcash3.location.LocationProvider;
import dal.cs.quickcash3.location.MockLocationProvider;
import dal.cs.quickcash3.permission.FragmentPermissionActivity;

public class WorkerDashboard extends FragmentPermissionActivity {
    private final String LOG_TAG = "WorkerDashboard";
    private Database database;
    private LocationProvider locationProvider;
    private Fragment receiptsFragment;
    private Fragment searchFragment;
    private Fragment mapFragment;
    private Fragment profileFragment;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_worker);

        initInterfaces();

        // Initialize the fragments.
        receiptsFragment = new ReceiptsFragment();
        searchFragment = new SearchFragment(database, locationProvider);
        mapFragment = new MapsFragment();
        profileFragment = new ProfileFragment();

        BottomNavigationView workerNavView = findViewById(R.id.workerBottomNavView);

        workerNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.workerReceiptPage) {
                Log.v(LOG_TAG, "Showing receipt fragment");
                replaceFragment(receiptsFragment);
                return true;
            }
            else if (itemId == R.id.workerSearchPage) {
                Log.v(LOG_TAG, "Showing search fragment");
                replaceFragment(searchFragment);
                return true;
            }
            else if (itemId == R.id.workerMapPage) {
                Log.v(LOG_TAG, "Showing map fragment");
                replaceFragment(mapFragment);
                return true;
            }
            else if (itemId == R.id.workerProfilePage) {
                Log.v(LOG_TAG, "Showing profile fragment");
                replaceFragment(profileFragment);
                return true;
            }
            else {
                Log.w(LOG_TAG, "Unrecognized item ID: " + itemId);
                return false;
            }
        });

        workerNavView.setSelectedItemId(R.id.workerMapPage);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.workerFragmentView, fragment);
        transaction.commit();
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
            database = new MyFirebaseDatabase(this);
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