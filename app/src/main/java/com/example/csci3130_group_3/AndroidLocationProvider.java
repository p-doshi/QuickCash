package com.example.csci3130_group_3;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull; // Optional
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.app.Activity;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;


public class AndroidLocationProvider implements LocationProvider {

    private Context context;
    private LocationManager locationManager;
    private Activity activity;
    private Location currentLocation;


    private static final long MIN_TIME_BW_UPDATES = 1;
    private static final float MIN_DISTANCE_CHANGES_FOR_UPDATES = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 87;

    // Constructor for the AndroidLocationProvider class
    public AndroidLocationProvider(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        requestLocationAccess();
    }
    @Override
    public int checkLocationPermissionsEnabled() {
        boolean isFineGranted = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isFineGranted) {
            return 2;
        }

        boolean isCoarseGranted = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isCoarseGranted) {
            return 1;
        }
        // Only return 0 if neither are enabled
        return 0;
    }
    /**
     * Requests location access if not yet granted. Otherwise does nothing
     */
    private void requestLocationAccess() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] requiredPerms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(activity, requiredPerms, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    // Android Studio wants EXPLICIT permission checking in the code
    // I have functions for that so I'll just suppress the warning.
    @SuppressLint("MissingPermission")
    @Override
    public Location getLocationPing() {
        // Double check we have location Access
        requestLocationAccess();

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return currentLocation;
    }
    @SuppressLint("MissingPermission")
    public Location getLocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (checkLocationPermissionsEnabled() == 0) {
            // No location provider is enabled, so return NULL
            return null;
        } else {
            // This starts a ping system that constantly checks for location updates
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGES_FOR_UPDATES,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            // When location is changed, update location
                            currentLocation = location;
                        }
                    }
            );

            // Grabs the current location as a quick reference
            currentLocation = getLocationPing();
            return currentLocation;
        }
    }
}