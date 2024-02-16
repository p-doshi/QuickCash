package com.example.csci3130_group_3;

import android.annotation.SuppressLint;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class AndroidLocationProvider implements LocationProvider {

    private Context context;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private Activity activity;
    private Location currentLocation = null;


    /*private static final long MIN_TIME_BW_UPDATES = 1;
    private static final float MIN_DISTANCE_CHANGES_FOR_UPDATES = 1;*/
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 87;


    // Constructor for the AndroidLocationProvider class
    public AndroidLocationProvider(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean checkLocationPermissionsEnabled() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }

    // Send out async request for permissions.
    public void requestLocationPermissions() {
        if (!checkLocationPermissionsEnabled()) {
            String[] requiredPerms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(activity, requiredPerms, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(1000)
                .setIntervalMillis(10000)
                .setMinUpdateIntervalMillis(5000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();
    }

    protected void checkLocationSettingsEnabled() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] perms, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                locationPing();

            } else {
                // Permission was not granted, alert user and degrade app.
                // This doesn't display for some reason
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Android can't handle functions checking permissions, so had to ignore the warning
    @SuppressLint("MissingPermission")
    @Override
    public void locationPing() {
        if (checkLocationPermissionsEnabled()) {
            locationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLocation = location;
                    } else {
                        // Couldn't get current location for some reason.
                    }
                }
            });
        } else {
            // If we don't have access to location we can't ping it, so request location perms.
            requestLocationPermissions();
        }
    }

    /*
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
    }*/
}