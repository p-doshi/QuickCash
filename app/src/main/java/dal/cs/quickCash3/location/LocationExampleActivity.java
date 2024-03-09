package dal.cs.quickCash3.location;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import dal.cs.quickCash3.R;
import dal.cs.quickCash3.location.AndroidLocationProvider;

public class LocationExampleActivity extends AppCompatActivity {

    // Creating an AndroidLocationProvider object immediately, in final build we'd create on map screen
    AndroidLocationProvider locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);

        setupLocation();
        setupDetectLocationButton();
        //setupTrackingButton();
        updateStatus();
    }

    protected void setupLocation() {
        locationProvider = new AndroidLocationProvider(this, this);
    }

    // Order of execution:
    // createLocationRequest() > requestLocationSettingsEnable() > requestLocationPermissions() > locationPing()

    protected void updateStatus() {
        TextView status = findViewById(R.id.status);
        if (locationProvider.checkLocationPermissionsEnabled()) {
            status.setText("Location Permissions: Granted");
        }
    }

    protected void updateLongLat() {
        TextView latTextbox = findViewById(R.id.latText);
        TextView longTextbox = findViewById(R.id.longText);
        String latOutput;
        String longOutput;
        if (locationProvider.checkLocationPermissionsEnabled() && locationProvider.getCurrentLocation() != null) {
            latOutput = "Latitude: "+locationProvider.getCurrentLocation().getLatitude();
            longOutput = "Longitude: "+locationProvider.getCurrentLocation().getLongitude();
        } else {
            latOutput = "Latitude: PENDING...";
            longOutput = "Longitude: PENDING...";
        }
        latTextbox.setText(latOutput);
        longTextbox.setText(longOutput);
    }

    protected void setupDetectLocationButton() {
        Button detectButton = findViewById(R.id.detectButton);
        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationProvider.setupLocationPermsSettings();
                if (locationProvider.checkLocationPermissionsEnabled()) {
                    locationProvider.locationPing();
                    updateStatus();
                    updateLongLat();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 87) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationProvider.locationPing();
            } else {
                //Permission was not granted, display toast and degrade app quality.
                Toast.makeText(this, "Location Permissions Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}