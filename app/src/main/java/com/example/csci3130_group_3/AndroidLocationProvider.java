package com.example.csci3130_group_3;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.List;
import java.util.function.Consumer;


public class AndroidLocationProvider implements LocationProvider {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 87;
    private final FusedLocationProviderClient locationProviderClient;
    private final Activity activity;
    private Consumer<Location> pendingLocationFunction;
    private Consumer<String> pendingErrorFunction;
    private boolean pendingPermissionRequest = false;


    // Constructor for the AndroidLocationProvider class
    public AndroidLocationProvider(@NonNull Activity activity) {
        this.activity = activity;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    @Override
    public void fetchLocation(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction) {
        if (pendingPermissionRequest) {
            errorFunction.accept(activity.getString(R.string.error_location_spam));
            return;
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Store the callbacks for the permission result.
            pendingLocationFunction = locationFunction;
            pendingErrorFunction = errorFunction;
            pendingPermissionRequest = true;

            // Send out an asynchronous request for location access.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

            return;
        }

        locationProviderClient.getLastLocation()
            .addOnCompleteListener(task -> {
                Location location = task.getResult();
                if (location == null) {
                    fetchNewLocation(locationFunction, errorFunction);
                } else {
                    locationFunction.accept(location);
                }
            })
            .addOnFailureListener(exception -> errorFunction.accept(exception.getMessage()));
    }

    private void fetchNewLocation(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction) {
        LocationRequest locationRequest =
            new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10)
                .build();

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                List<Location> locations = locationResult.getLocations();
                if (locations.isEmpty()) {
                    return;
                }

                Location location = locations.get(locations.size() - 1);
                if (location == null) {
                    return;
                }

                locationFunction.accept(location);
                locationProviderClient.removeLocationUpdates(this);
            }
        };

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Store the callbacks for the permission request result.
            pendingLocationFunction = locationFunction;
            pendingErrorFunction = errorFunction;
            pendingPermissionRequest = true;

            // Send out an asynchronous request for location access.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

            return;
        }

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, activity.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        pendingPermissionRequest = false;

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