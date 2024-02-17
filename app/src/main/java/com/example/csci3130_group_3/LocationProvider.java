package com.example.csci3130_group_3;

import android.location.Location;

public interface LocationProvider {
    /**
     * Checks if fine location permissions have been granted
     * @return True if they have been granted, false otherwise
     */
    boolean checkLocationPermissionsEnabled();

    /**
     * Starts Async requests to initialize
     * location settings and permissions in the app
     */
    public void setupLocationPermsSettings();

    /**
     * Getter for currentLocation tracked in locationProvider
     * @return Location object
     */
    Location getCurrentLocation();

    /**
     * Asynchronously requests a location ping
     * and updates currentLocation.
     */
    void locationPing();

    /*
     * Catches the permission request's results
     * @param requestCode The request code for the permission to be granted
     * @param perms List of permissions requested
     * @param grantResults If the permission was granted
     */
    //void onRequestPermissionsResult(int requestCode, String[] perms, int[] grantResults);
}
