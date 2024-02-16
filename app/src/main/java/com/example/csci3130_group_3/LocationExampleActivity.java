package com.example.csci3130_group_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

// TODO: Do not commit this file with the changes
public class LocationExampleActivity extends AppCompatActivity {

    // Creating an AndroidLocationProvider object immediately, in final build we'd create on map screen
    AndroidLocationProvider locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);

        setupLocation();
        setupDetectLocationButton();
        setupTrackingButton();
    }

    protected void setupLocation() {
        locationProvider = new AndroidLocationProvider(this, this);
    }

    protected void updateStatus() {
        TextView status = findViewById(R.id.status);
        if (locationProvider.checkLocationPermissionsEnabled()) {
            status.setText("Location Detected Successfully");
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
            latOutput = "Latitude: UNABLE TO FIND";
            longOutput = "Longitude: UNABLE TO FIND";
        }
        latTextbox.setText(latOutput);
        longTextbox.setText(longOutput);
    }

    protected void setupDetectLocationButton() {
        Button detectButton = findViewById(R.id.detectButton);
        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationProvider.checkLocationPermissionsEnabled()) {
                    // Location Permissions are enabled, ping the location
                    locationProvider.locationPing();
                    updateStatus();
                    updateLongLat();
                } else {
                    locationProvider.requestLocationPermissions();
                }
            }
        });
    }

    protected void setupTrackingButton() {
        Button trackingButton = findViewById(R.id.trackingButton);
        trackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Don't do anything yet
            }
        });
    }

    // This is an ugly way to update the status bar, but having trouble implementing in location provider
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 87) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationProvider.locationPing();
                updateStatus();
                updateLongLat();
            }
        }
    }
}