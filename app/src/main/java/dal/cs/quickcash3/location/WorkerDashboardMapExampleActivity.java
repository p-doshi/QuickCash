package dal.cs.quickcash3.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.permission.AppCompatPermissionActivity;

public class WorkerDashboardMapExampleActivity extends AppCompatPermissionActivity {

    @SuppressWarnings("PMD.LawOfDemeter") // This is how R.id is meant to be used.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dashboard_worker);

        BottomNavigationView workerNavView = findViewById(R.id.workerBottomNavView);

        // Upon loading the worker page it should immediately request location permissions and get current location
        LocationProvider locationProvider = new AndroidLocationProvider(this, 5000);
        // Need to request location here
        locationProvider.fetchLocation(
                location -> {
                    // Nothing should happen
                },
                error -> {
                    // If location was not granted that's fine
                }
        );

        // Sets up a detector to check when navigation bar items clicked
        workerNavView.setOnItemSelectedListener(item -> {
            FragmentManager fragmentManger = getSupportFragmentManager();
            Fragment currentFragment = fragmentManger.findFragmentById(R.id.mapContainer);

            if (item.getItemId() == R.id.workerMapPage && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // If they selected map with location enabled display the map fragment
                MapFragment fragment = new MapFragment();
                // For the sake of consistent tests (We don't know where the CI is) we'll use a static location while testing
                assignTestValues(fragment);
                fragmentManger.beginTransaction().add(R.id.mapContainer, fragment).commit();
            }
            else if (item.getItemId() == R.id.workerMapPage && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // If they selected the map WITHOUT location permissions, show them a toast that it won't work
                Toast.makeText(this, "Location Permissions must be enabled to use Map", Toast.LENGTH_LONG).show();
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
    private static void assignTestValues(@NonNull MapFragment fragment) {
        // Sets a testing location as the current location
        fragment.setCurrentLocation(new LatLng(44.6356, -63.5952));

        // Create an array of 2 pairs of jobs
        fragment.addJob("Job 1", new LatLng(44.641718, -63.584126));
        fragment.addJob("Job 2", new LatLng(44.6398496, -63.601291));
    }
}
