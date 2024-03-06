package com.example.csci3130_group_3;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;


public class AndroidLocationProvider implements LocationProvider {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 87;
    private final FusedLocationProviderClient locationProviderClient;
    private final Activity activity;
    private final long updateFrequencyMillis;
    private final AtomicReference<Location> lastLocation = new AtomicReference<>();
    private final AtomicBoolean hasPendingLocation = new AtomicBoolean(false);
    private Consumer<Location> pendingLocationFunction;
    private Consumer<String> pendingErrorFunction;


    // Constructor for the AndroidLocationProvider class
    public AndroidLocationProvider(@NonNull Activity activity, long updateFrequencyMillis) {
        this.activity = activity;
        this.updateFrequencyMillis = updateFrequencyMillis;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    @Override
    public void fetchLocation(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction) {
        if (hasPendingLocation.get()) {
            errorFunction.accept(activity.getString(R.string.error_location_spam));
            return;
        }

        if (lastLocation.get() == null) {
            fetchNewLocation(locationFunction, errorFunction);
            return;
        }

        locationFunction.accept(lastLocation.get());
    }

    private void fetchNewLocation(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction) {
        pendingLocationFunction = locationFunction;
        pendingErrorFunction = errorFunction;

        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            hasPendingLocation.set(false);

            // Send an asynchronous request for location access.
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

            return;
        }

        LocationRequest locationRequest =
            new LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, updateFrequencyMillis)
                .build();

        LocationCallback locationCallback = new MyLocationCallback(
            locationProviderClient,
            location -> {
                lastLocation.set(location);
                if (hasPendingLocation.get()) {
                    pendingLocationFunction.accept(location);
                    hasPendingLocation.set(false);
                }
            });

        hasPendingLocation.set(true);

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, activity.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.length > 0 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            fetchLocation(pendingLocationFunction, pendingErrorFunction);
        }
        else {
            pendingErrorFunction.accept(activity.getString(R.string.error_location_permission));
        }
    }
}