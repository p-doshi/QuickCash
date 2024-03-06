package com.example.csci3130_group_3;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.function.Consumer;


public class AndroidLocationProvider implements LocationProvider {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 87;
    private final FusedLocationProviderClient locationProviderClient;
    private final Activity activity;

    // These will not be null if we have a pending location permission request.
    private Consumer<Location> locationFunction;
    private Consumer<String> errorFunction;


    // Constructor for the AndroidLocationProvider class
    public AndroidLocationProvider(@NonNull Activity activity) {
        this.activity = activity;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    @Override
    public void fetchLocation(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction) {
        if (this.locationFunction != null) {
            throw new RuntimeException("Cannot fetch location before previous fetch completes");
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)   != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Send out an asynchronous request for location access.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

            // Store the callbacks for the permission result.
            this.locationFunction = locationFunction;
            this.errorFunction = errorFunction;

            return;
        }

        locationProviderClient.getLastLocation()
            .addOnCompleteListener(task -> {
                Location location = task.getResult();
                if (location == null) {
                    errorFunction.accept(activity.getString(R.string.error_null_location));
                }
                locationFunction.accept(location);
            })
            .addOnFailureListener(exception -> this.errorFunction.accept(exception.getMessage()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Get the callbacks inputs to teh original location request.
        Consumer<Location> locationFunction = this.locationFunction;
        Consumer<String> errorFunction = this.errorFunction;
        this.locationFunction = null;
        this.errorFunction = null;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.length > 0 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            fetchLocation(locationFunction, errorFunction);
        }
        else {
            errorFunction.accept(activity.getString(R.string.error_location_permission));
        }
    }
}