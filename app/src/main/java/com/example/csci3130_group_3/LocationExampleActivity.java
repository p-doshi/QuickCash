package com.example.csci3130_group_3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class LocationExampleActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 87;
    // Creating an AndroidLocationProvider object immediately, in final build we'd create on map screen
    private AndroidLocationProvider locationProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_example);

        setupLocation();
        setupDetectLocationButton();
        //setupTrackingButton();
        updateStatus();
    }

    protected void setupLocation() {
        locationProvider = new AndroidLocationProvider(this);
    }

    // Order of execution:
    // createLocationRequest() > requestLocationSettingsEnable() > requestLocationPermissions() > locationPing()

    @SuppressLint("SetTextI18n")
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationProvider.locationPing();
            } else {
                //Permission was not granted, display toast and degrade app quality.
                Toast.makeText(this, "Location Permissions Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}