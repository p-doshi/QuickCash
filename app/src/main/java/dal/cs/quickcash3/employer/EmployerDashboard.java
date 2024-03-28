package dal.cs.quickcash3.employer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Set;
import java.util.TreeSet;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.fragments.ProfileFragment;
import dal.cs.quickcash3.fragments.ReceiptsFragment;
import dal.cs.quickcash3.geocode.GeocoderProxy;
import dal.cs.quickcash3.geocode.MockGeocoder;
import dal.cs.quickcash3.geocode.MyGeocoder;


public class EmployerDashboard extends FragmentActivity {
    Database database;
    MyGeocoder geocoder;
    private static final String LOG_TAG = EmployerDashboard.class.getSimpleName();

    @SuppressWarnings("PMD.LawOfDemeter") // There is no other way to do this.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_employer);

        initInterfaces();

        // Initialize the fragments.
        Fragment receiptsFragment = new ReceiptsFragment();
        Fragment profileFragment = new ProfileFragment();
        Fragment jobPostFormFragment = new PostJobForm(database, geocoder);

        BottomNavigationView employerNavView = findViewById(R.id.employerBottomNavView);

        employerNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            //TODO change to employer
            if (itemId == R.id.workerReceiptPage) {
                Log.v(LOG_TAG, "Showing receipt fragment");
                replaceFragment(receiptsFragment);
                return true;
            }
            else if (itemId == R.id.employer_add_job) {
                Log.v(LOG_TAG, "Showing post job fragment");
                replaceFragment(jobPostFormFragment);
                return true;
            }
            else if (itemId == R.id.workerProfilePage) {
                Log.v(LOG_TAG, "Showing profile fragment");
                replaceFragment(profileFragment);
                return true;
            }
            else {
                throw new IllegalArgumentException("Unrecognized item ID: " + itemId);
            }
        });

        employerNavView.setSelectedItemId(R.id.employer_add_job);
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.employerDashFragment, fragment);
        transaction.commit();
    }


    /**
     * Initialize mock database
     */
    @SuppressLint("RestrictedApi")
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

        if (categories.contains(getString(R.string.MOCK_GEOCODER))) {
            geocoder = new MockGeocoder();
            Log.d(LOG_TAG, "Using Mock Geocoder");
        }
        else {
            geocoder = new GeocoderProxy(this);
        }
    }

    public @NonNull MyGeocoder getGeocoder() {
        return geocoder;
    }

    /**
     * Get the mock database
     * @return database
     */
    public @NonNull Database getDatabase(){
        return database;
    }

}