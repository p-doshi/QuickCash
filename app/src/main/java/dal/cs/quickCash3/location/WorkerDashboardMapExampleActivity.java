package dal.cs.quickCash3.location;

import android.location.Location;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dal.cs.quickCash3.R;

import android.util.Pair;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class WorkerDashboardMapExampleActivity extends FragmentActivity {
    LocationProvider locationProvider;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_worker);

        BottomNavigationView workerNavView = findViewById(R.id.workerBottomNavView);

        // Upon loading the worker page it should immediately request location permissions and get current location
        locationProvider = new AndroidLocationProvider(this, this);
        locationProvider.setupLocationPermsSettings();
        locationProvider.locationPing();

        // Sets up a detector to check when navigation bar items clicked
        workerNavView.setOnItemSelectedListener(item -> {
            FragmentManager fragmentManger = getSupportFragmentManager();
            Fragment currentFragment = fragmentManger.findFragmentById(R.id.mapContainer);

            if (item.getItemId() == R.id.workerMapPage && locationProvider.checkLocationPermissionsEnabled()) {
                // If they selected map with location enabled display the map fragment
                MapFragment fragment = new MapFragment();
                // For the sake of consistent tests (We don't know where the CI is) we'll use a static location while testing
                assignTestValues(fragment);
                fragmentManger.beginTransaction().replace(R.id.mapContainer, fragment).commit();
            }
            else if (item.getItemId() == R.id.workerMapPage && !locationProvider.checkLocationPermissionsEnabled()) {
                // If they selected the map WITHOUT location permissions, show them a toast that it won't work
                Toast.makeText(WorkerDashboardMapExampleActivity.this,
                        "Location Permissions must be enabled to use Map", Toast.LENGTH_LONG).show();
            }
            else if (currentFragment != null) {
                // Detaches current fragment if the user clicks another menu option
                // Will be replaced with other tabs eventually
                fragmentManger.beginTransaction().detach(currentFragment).commit();
            }
            return true;
        });
    }

    // Only used for the test Demo
    private static void assignTestValues(MapFragment fragment) {
        // Sets a testing location as the current location
        Location currentLocationTesting = new Location("Testing Location");
        currentLocationTesting.setLatitude(44.6356);
        currentLocationTesting.setLongitude(-63.5952);
        fragment.setCurrentLocation(currentLocationTesting);

        // Create an array of 2 pairs of jobs
        Location job1Location = new Location("Job 1 Location");
        job1Location.setLatitude(44.641718);
        job1Location.setLongitude(-63.584126);
        Location job2Location = new Location("Job 2 Location");
        job2Location.setLatitude(44.6398496);
        job2Location.setLongitude(-63.601291);

        Pair<Location, String>[] jobs = new Pair[] {
                new Pair(job1Location, "Job 1"),
                new Pair(job2Location, "Job 2")
        };
        fragment.setJobList(jobs);
    }


}
