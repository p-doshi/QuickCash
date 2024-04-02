package dal.cs.quickcash3.employer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.annotations.Nullable;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.fragments.ProfileFragment;
import dal.cs.quickcash3.fragments.ReceiptsFragment;
import dal.cs.quickcash3.jobs.JobListingsFragment;
import dal.cs.quickcash3.geocode.GeocoderProxy;
import dal.cs.quickcash3.geocode.MockGeocoder;
import dal.cs.quickcash3.geocode.MyGeocoder;
import dal.cs.quickcash3.search.RegexSearchFilter;

public class EmployerDashboard extends AppCompatActivity {
    private static final String LOG_TAG = EmployerDashboard.class.getSimpleName();
    private Database database;
    private MyGeocoder geocoder;

    @SuppressWarnings("PMD.LawOfDemeter") // There is no other way to do this.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_employer);

        initInterfaces();

        // Get a search filter for the current user.
        String currentUser = getIntent().getStringExtra(getString(R.string.USER));
        RegexSearchFilter<AvailableJob> searchFilter = new RegexSearchFilter<>("employer");
        if (currentUser == null) {
            searchFilter.setPattern(Pattern.compile(".*"));
        }
        else {
            searchFilter.setPattern(Pattern.compile(currentUser));
        }

        // Initialize the fragments.
        Fragment listingsFragment = new JobListingsFragment(database, searchFilter, this::showJobPostForm);
        Fragment receiptsFragment = new ReceiptsFragment();
        Fragment profileFragment = new ProfileFragment();

        BottomNavigationView employerNavView = findViewById(R.id.employerBottomNavView);

        employerNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.employer_job_listings) {
                replaceFragment(listingsFragment);
                return true;
            }
            else if (itemId == R.id.employer_receipts) {
                replaceFragment(receiptsFragment);
                return true;
            }
            else if (itemId == R.id.employer_profile) {
                replaceFragment(profileFragment);
                return true;
            }
            else {
                throw new IllegalArgumentException("Unrecognized item ID: " + itemId);
            }
        });

        employerNavView.setSelectedItemId(R.id.employer_job_listings);
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        Log.v(LOG_TAG, "Showing " + fragment.getClass().getSimpleName() + " fragment");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.employerFragmentView, fragment);
        transaction.commit();
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod") // This is used.
    private void showJobPostForm() {
        Fragment jobPostFormFragment = new PostJobForm(database, geocoder);
        replaceFragment(jobPostFormFragment);
    }

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

    public @NonNull Database getDatabase(){
        return database;
    }
}
