package dal.cs.quickcash3.employer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.annotations.Nullable;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.data.Worker;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.data.Worker;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.fragments.ProfileFragment;
import dal.cs.quickcash3.fragments.HistoryFragment;
import dal.cs.quickcash3.fragments.HistoryFragment;
import dal.cs.quickcash3.geocode.GeocoderProxy;
import dal.cs.quickcash3.geocode.MockGeocoder;
import dal.cs.quickcash3.geocode.MyGeocoder;
import dal.cs.quickcash3.jobdetail.ApplicantsFragment;
import dal.cs.quickcash3.jobdetail.JobDetailsPage;
import dal.cs.quickcash3.payment.EmployerPayPal;
import dal.cs.quickcash3.payment.MockPayment;
import dal.cs.quickcash3.payment.PayPalPaymentProcess;
import dal.cs.quickcash3.payment.Payment;
import dal.cs.quickcash3.payment.PaymentConfirmationActivity;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.util.BackButtonListener;

public class EmployerDashboard extends AppCompatActivity {
    private static final String LOG_TAG = EmployerDashboard.class.getSimpleName();
    private final AtomicReference<Runnable> pendingWork = new AtomicReference<>(null);
    private final AtomicReference<Runnable> pendingWork = new AtomicReference<>(null);
    private Database database;
    private MyGeocoder geocoder;
    private Fragment historyFragment;
    private Fragment listingFragment;
    private Payment payment;

    @SuppressWarnings("PMD.LawOfDemeter") // There is no other way to do this.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_employer);

        initInterfaces();

        // Get a search filter for the current user.
        String userId = getIntent().getStringExtra(getString(R.string.USER));
        RegexSearchFilter<AvailableJob> availableJobSearchFilter = new RegexSearchFilter<>(AvailableJob::getEmployer);
        RegexSearchFilter<CompletedJob> completedJobSearchFilter = new RegexSearchFilter<>(CompletedJob::getEmployer);
        if (userId == null) {
            availableJobSearchFilter.setPattern(Pattern.compile(".*"));
            completedJobSearchFilter.setPattern(Pattern.compile(".*"));
        String userId = getIntent().getStringExtra(getString(R.string.USER));
        RegexSearchFilter<AvailableJob> availableJobSearchFilter = new RegexSearchFilter<>(AvailableJob::getEmployer);
        RegexSearchFilter<CompletedJob> completedJobSearchFilter = new RegexSearchFilter<>(CompletedJob::getEmployer);
        if (userId == null) {
            availableJobSearchFilter.setPattern(Pattern.compile(".*"));
            completedJobSearchFilter.setPattern(Pattern.compile(".*"));
        }
        else {
            availableJobSearchFilter.setPattern(Pattern.compile(userId));
            completedJobSearchFilter.setPattern(Pattern.compile(userId));
            availableJobSearchFilter.setPattern(Pattern.compile(userId));
            completedJobSearchFilter.setPattern(Pattern.compile(userId));
        }

        // Initialize the fragments.
        listingFragment = new EmployerListingFragment(
                this, database, availableJobSearchFilter, this::showJobPostForm, this::showAvailableJobDetails);
        historyFragment = new HistoryFragment(
                this, database, completedJobSearchFilter, this::showCompletedJobDetails);
        Fragment profileFragment = new ProfileFragment();

        BottomNavigationView employerNavView = findViewById(R.id.employerBottomNavView);

        employerNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.employer_job_listings) {
                replaceFragment(listingFragment);
                replaceFragment(listingFragment);
                return true;
            }
            else if (itemId == R.id.employer_history) {
                replaceFragment(historyFragment);
            else if (itemId == R.id.employer_history) {
                replaceFragment(historyFragment);
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

    @Override
    protected void onDestroy() {
        runPendingWork();
        super.onDestroy();
    }

    private void runPendingWork() {
        Runnable function = pendingWork.getAndSet(null);
        if (function != null) {
            Log.i(LOG_TAG, "Running pending work");
            function.run();
        }
    }

    @Override
    protected void onDestroy() {
        runPendingWork();
        super.onDestroy();
    }

    private void runPendingWork() {
        Runnable function = pendingWork.getAndSet(null);
        if (function != null) {
            Log.i(LOG_TAG, "Running pending work");
            function.run();
        }
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        Log.i(LOG_TAG, "Showing " + fragment.getClass().getSimpleName());
        runPendingWork();
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.employerFragmentView, fragment);
        transaction.commit();
    }

    private void addFragment(@NonNull Fragment fragment) {
        Log.i(LOG_TAG, "Showing " + fragment.getClass().getSimpleName());
        runPendingWork();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.employerFragmentView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showJobPostForm() {
        Fragment jobPostFormFragment = new PostJobForm(database, geocoder);
        addFragment(jobPostFormFragment);
    }

    private void showAvailableJobDetails(@NonNull AvailableJob job) {
        Fragment applicantsFragment = new ApplicantsFragment(database, job, worker -> acceptJob(job, worker));
        Fragment jobDetailsPage = new JobDetailsPage(job, applicantsFragment);
        addFragment(jobDetailsPage);
        pendingWork.set(() -> job.writeToDatabase(database, error -> Log.e(LOG_TAG, "Saving job: " + error)));
    }

    private void showCompletedJobDetails(@NonNull CompletedJob job) {
        Fragment jobDetailsPage = new JobDetailsPage(job, null);
        addFragment(jobDetailsPage);
    }

    private void acceptJob(@NonNull AvailableJob availableJob, @NonNull Worker worker) {

        Fragment makePayment = new EmployerPayPal(payment,
                payID -> {
                    Intent paymentConfirmationIntent = new Intent(this, PaymentConfirmationActivity.class);

                    paymentConfirmationIntent.putExtra(getString(R.string.PAY_ID), payID);

                    CompletedJob completedJob = CompletedJob.completeJob(availableJob, worker);
                    completedJob.setPayId(payID);

                    availableJob.deleteFromDatabase(database,
                            error -> Log.e(LOG_TAG, "Deleting job: " + error));
                    completedJob.writeToDatabase(database,
                            error -> Log.e(LOG_TAG, "Creating completed job: " + error));

                    replaceFragment(listingFragment);
                    startActivity(paymentConfirmationIntent);
                },
                error -> {
                    if (error.equals(getString(R.string.payment_cancelled))) {
                        getSupportFragmentManager().popBackStack();
                    } else {
                        Log.e(LOG_TAG, "Payment failure: " + error);
                        error = "Payment failure";
                    }
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                });
        addFragment(makePayment);
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

        if (categories.contains(getString(R.string.MOCK_GEOCODER))) {
            geocoder = new MockGeocoder();
            Log.i(LOG_TAG, "Using Mock Geocoder");
            Log.i(LOG_TAG, "Using Mock Geocoder");
        }
        else {
            geocoder = new GeocoderProxy(this);
        }

        if (categories.contains(getString(R.string.MOCK_PAYMENT))) {
            payment =  new MockPayment();
            Log.i(LOG_TAG, "Using Mock Payment");
        }
        else {
            payment =  new PayPalPaymentProcess(this);
        }
    }

    public @NonNull MyGeocoder getGeocoder() {
        return geocoder;
    }

    public @NonNull Database getDatabase(){
        return database;
    }

    public @NonNull Payment getPayment(){
        return payment;
    }
}
