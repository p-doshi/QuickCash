package com.example.csci3130_group_3;

import android.annotation.SuppressLint;
import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class AndroidLocationProvider implements LocationProvider {

    private Context context;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private Activity activity;
    private Location currentLocation = null;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 87;
    private static final int REQUEST_CHECK_SETTINGS = 117;


    // Constructor for the AndroidLocationProvider class
    public AndroidLocationProvider(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void setupLocationPermsSettings() {
        createLocationRequest();
        requestLocationSettingsEnable(true);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean checkLocationPermissionsEnabled() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Send out async request for permissions.
    private void requestLocationPermissions() {
        if (!checkLocationPermissionsEnabled()) {
            String[] requiredPerms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(activity, requiredPerms, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(1000)
                .setIntervalMillis(10000)
                .setMinUpdateIntervalMillis(5000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();
    }

    /**
     * Checks if location setting is enabled
     * If it isn't, create a popup requesting that they enable it.
     * @param continueToPerms If true, proceed with also requesting location permissions
     */
    private void requestLocationSettingsEnable(boolean continueToPerms) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // Location setting was already enabled
                if (continueToPerms) {
                    requestLocationPermissions();
                }
            }
        });
        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location Settings are not enabled, show a popup requesting them
                    try {
                        // Show dialogue box requesting enable location settings
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity,
                                REQUEST_CHECK_SETTINGS);
                        // Once location setting enabled, if we're booting system then requestLocationPermissions
                        if (continueToPerms) {
                            requestLocationPermissions();
                        }
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Do nothing with the error
                    }
                }
            }
        });
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
                        // If we couldn't get the location, alert the user!
                        Toast.makeText(context, "Couldn't Access Location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // If we don't have access to location we can't ping it, so request location perms.
            requestLocationPermissions();
        }
    }
}