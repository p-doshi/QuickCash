package dal.cs.quickcash3.employer;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.data.Worker;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.fragments.ProfileFragment;
import dal.cs.quickcash3.fragments.ReceiptsFragment;
import dal.cs.quickcash3.geocode.GeocoderProxy;
import dal.cs.quickcash3.geocode.MockGeocoder;
import dal.cs.quickcash3.geocode.MyGeocoder;
import dal.cs.quickcash3.jobdetail.ApplicantsFragment;
import dal.cs.quickcash3.jobdetail.JobDetailsPage;
import dal.cs.quickcash3.payment.EmployerPaymentConfirmationActivity;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.util.BackButtonListener;

public class EmployerDashboard extends AppCompatActivity {
    private static final String LOG_TAG = EmployerDashboard.class.getSimpleName();
    private final AtomicReference<Runnable> pendingWork = new AtomicReference<>(null);
    private Database database;
    private MyGeocoder geocoder;
    private Fragment listingFragment;

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
        listingFragment = new EmployerListingFragment(this, database, searchFilter, this::showJobPostForm, this::switchToJobDetails);
        Fragment receiptsFragment = new ReceiptsFragment();
        Fragment profileFragment = new ProfileFragment();

        BottomNavigationView employerNavView = findViewById(R.id.employerBottomNavView);

        employerNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.employer_job_listings) {
                replaceFragment(listingFragment);
                return true;
            }
            else if (itemId == R.id.employer_history) {
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
        Log.v(LOG_TAG, "Showing " + fragment.getClass().getSimpleName());
        Runnable function = pendingWork.getAndSet(null);
        if (function != null) {
            Log.v(LOG_TAG, "Running pending work");
            function.run();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.employerFragmentView, fragment);
        transaction.commit();
    }

    private void showJobPostForm() {
        Fragment jobPostFormFragment = new PostJobForm(database, geocoder);
        replaceFragment(jobPostFormFragment);
        getOnBackPressedDispatcher().addCallback(jobPostFormFragment,
            new BackButtonListener(() -> replaceFragment(listingFragment)));
    }

    private void switchToJobDetails(@NonNull AvailableJob job) {
        Fragment applicantsFragment = new ApplicantsFragment(database, job, worker -> acceptJob(job, worker));
        Fragment jobDetailsPage = new JobDetailsPage(job, applicantsFragment);
        replaceFragment(jobDetailsPage);
        pendingWork.set(() -> job.writeToDatabase(database, error -> Log.e(LOG_TAG, "Saving job: " + error)));
        getOnBackPressedDispatcher().addCallback(jobDetailsPage,
            new BackButtonListener(() -> replaceFragment(listingFragment)));
    }

    private void acceptJob(@NonNull AvailableJob availableJob, @NonNull Worker worker) {
        Intent paymentConfirmationIntent = new Intent(getBaseContext(), EmployerPaymentConfirmationActivity.class);

        // TODO: Set this up properly.
        String payId = "lkajsdoiuqdlkaklsjdq";
        String status = "approved";
        paymentConfirmationIntent.putExtra("PAY_ID", payId);
        paymentConfirmationIntent.putExtra("STATUS", status);

        CompletedJob completedJob = CompletedJob.completeJob(availableJob, worker);
        completedJob.setPayId(payId);
        completedJob.setStatus(status);

        pendingWork.set(null);
        availableJob.deleteFromDatabase(database, error -> Log.e(LOG_TAG, "Deleting job: " + error));
        completedJob.writeToDatabase(database, error -> Log.e(LOG_TAG, "Creating completed job: " + error));

        startActivity(paymentConfirmationIntent);

        replaceFragment(listingFragment);
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
