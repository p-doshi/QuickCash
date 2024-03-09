package dal.cs.quickcash3.location;

import android.location.Location;

import androidx.annotation.Nullable;

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
    void setupLocationPermsSettings();

    /**
     * Getter for currentLocation tracked in locationProvider
     * @return Location object
     */
    @Nullable Location getCurrentLocation();

    /**
     * Asynchronously requests a location ping
     * and updates currentLocation.
     */
    void locationPing();
}
