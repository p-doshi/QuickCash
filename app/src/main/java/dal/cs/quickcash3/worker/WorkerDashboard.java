package dal.cs.quickcash3.worker;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.data.JobPost;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.ObjectSearchAdapter;
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
import dal.cs.quickcash3.search.LocationSearchFilter;
import dal.cs.quickcash3.search.SearchFilter;
import dal.cs.quickcash3.util.AsyncLatch;
import dal.cs.quickcash3.util.CustomObserver;
import java.util.stream.Collectors;

// There HAS to be a better way to do this!
public class WorkerDashboard<T extends JobPost> extends AppCompatPermissionActivity {
    private static final String LOG_TAG = WorkerDashboard.class.getSimpleName();
    private Database database;
    private LocationProvider locationProvider;
    private MapFragment mapFragment;
    private Fragment jobSearchFragment;
    // All used in map fragment
    private LocationSearchFilter<AvailableJob> locationFilter;
    private final AsyncLatch<SearchFilter<AvailableJob>> asyncFilter = new AsyncLatch<>();
    //private final Map<String,AvailableJob> availableJobMap = new HashMap<>();
    private final List<T> searchResults = new ArrayList<>();
    private final List<T> jobList = new ArrayList<>();
    private int callbackId;

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

        // Setup the location filter
        final int MAX_DISTANCE = 30;

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

                // Create a location filter with a max distance
                locationFilter = new LocationSearchFilter<>(AvailableJob::getLatitude, AvailableJob::getLongitude);
                locationFilter.setMaxDistance(MAX_DISTANCE);
                fetchLocationForFilter();

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

        // This might have issues with sync depending on what tab needs to appear first
        workerNavView.setSelectedItemId(R.id.workerHistoryPage);
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

    // This might be unnecessary, we're just creating a copy of LatLng location and feeding it back in
    private void showLocation(@NonNull LatLng location) {
        LatLng latlng = new LatLng(location.latitude, location.longitude);
        mapFragment.setCurrentLocation(latlng);
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void showLocationError(@NonNull String error) {
        Log.w(LOG_TAG, error);
    }

    private void fetchLocationForFilter() {
        locationProvider.fetchLocation(
                location -> {
                    Log.v("TESTING_TAG", "Location fetched successfully: " + location);
                    locationFilter.setLocation(location);
                    asyncFilter.set(locationFilter);
                    //fetchJobsFromDatabase();
                },
                error -> {
                    Log.w(LOG_TAG, error);
                    // Skip the location filter if we can't get a location.
                }
        );
    }

    // Search the availableJobMap for jobs that match the filter (nearby) and adds them to the map
    private void searchList(@NonNull SearchFilter<T> filter){
        List<T> newJobs = searchResults.stream().filter(filter::isValid).collect(Collectors.toList());
        for (T job : newJobs) {
            // Add the new jobs to the map
            LatLng jobLocation = new LatLng(job.getLatitude(), job.getLongitude());
            mapFragment.addJob(job.getTitle(),jobLocation);
        }
    }

    /* Previously failed iteration
    private void setFilterCallback() {
        asyncFilter.get(searchFilter -> {
            Log.i(LOG_TAG, "Starting database search listener");
            adapter.reset();
            searchResults.clear();

            ObjectSearchAdapter<T> searchAdapter = new ObjectSearchAdapter<>(searchFilter);
            searchAdapter.addObserver(new CustomObserver<>(adapter::addJob, adapter::removeJob));
            searchAdapter.addObserver(new ListObserver<>(searchResults));

            callbackId = database.addDirectoryListener(directory, type,
                    searchAdapter::receive,
                    error -> Log.w(LOG_TAG, "Received database error: " + error));
        });
    }*/

    private void fetchJobsFromDatabase() {
        String startDateId = ".*";
        RegexSearchFilter<AvailableJob> searchFilter = new RegexSearchFilter<>(AvailableJob::getStartDate);
        searchFilter.setPattern(Pattern.compile(startDateId));

        ObjectSearchAdapter<AvailableJob> searchAdapter = new ObjectSearchAdapter<>(searchFilter);
        searchAdapter.addObserver(new CustomObserver<>(this::addJob, this::removeWorker));

        callbackId = database.addDirectoryListener(CompletedJob.DIR, AvailableJob.class, searchAdapter::receive,
                error -> Log.w(LOG_TAG, "Received database error: " + error));
        //searchList((SearchFilter<T>) locationFilter); // Might work?
    }

    private void addJob(@NonNull AvailableJob job) {
        jobList.add((T) job);
    }

    private void removeWorker(@NonNull AvailableJob job) {
        jobList.remove((T) job);
    }
}