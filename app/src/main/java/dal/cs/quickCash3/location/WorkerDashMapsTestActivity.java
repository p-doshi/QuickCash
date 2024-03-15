package dal.cs.quickCash3.location;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import dal.cs.quickCash3.R;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class WorkerDashMapsTestActivity extends FragmentActivity {
    LocationProvider locationProvider;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.test_mapdashlayout); // Replace with dashboard_worker when done

        BottomNavigationView workerNavView = findViewById(R.id.workerBottomNavView);

        // Code below added by ~~~~~ for the map section
        // Upon loading the worker page it should immediately request location permissions and get current location
        locationProvider = new AndroidLocationProvider(this, this);
        locationProvider.setupLocationPermsSettings();
        locationProvider.locationPing();

        // Sets up a detector to check when navigation bar items clicked
        workerNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManger = getSupportFragmentManager();
                Fragment currentFragment = fragmentManger.findFragmentById(R.id.mapContainer);

                if (item.getItemId() == R.id.workerMapPage) {
                    // If they selected map, display the map fragment
                    fragmentManger.beginTransaction().replace(R.id.mapContainer, new MapsActivity()).commit();
                }
                else if (currentFragment != null) {
                    // Detaches current fragment if the user clicks another menu option
                    // Will be replaced with other tabs eventually
                    fragmentManger.beginTransaction().detach(currentFragment).commit();
                }
                return true;
            }
        });
    }


}
