package com.example.csci3130_group_3;

import android.location.Location;

public interface LocationProvider {

    // Constructor
    // Contains a Location currentLocation tracker that gets updated by locationPing()
    // public AndroidLocationProvider(Context context, Activity activity);

    /**
     * Checks if fine location permissions have been granted
     * @return True if they have been granted, false otherwise
     */
    boolean checkLocationPermissionsEnabled();

    /**
     * Sends out a system async request to request location access.
     */
    void requestLocationPermissions();

    /**
     * Catches the permission request's results
     * @param requestCode The request code for the permission to be granted
     * @param perms List of permissions requested
     * @param grantResults If the permission was granted
     */
    //void onRequestPermissionsResult(int requestCode, String[] perms, int[] grantResults);

    /**
     * Requests a location ping and updates currentLocation.
     */
    void locationPing();

    /**
     * Gets the currentLocation tracked in locationProvider
     * @return Location object
     */
    Location getCurrentLocation();

    // Need to figure this out, maybe I want to return Location instead of void?
    // I'm going to be calling this function in the app to display a map, IDK how to implement that
    //Location getLocation();
}
