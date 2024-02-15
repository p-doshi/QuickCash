package com.example.csci3130_group_3;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

// Unit tests can debug the logic
// So maybe they're the best choice for checking if location is enabled
public class LocationUnitTest {

    // Mock the Location Manager (Essentially spoofing it)
    @Mock
    private LocationManager locationManager;

    @Test
    public void testLocationPermissionEnabled() {
        when(locationManager.isLocationEnabled()).thenReturn(false);
    }

    @Test
    public void testLocationPermissionDisabled() {

    }

    // Try to get the location
    @Test
    public void getLocation() {
        // locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }



    /* So here's the jist.
    We need to use mockito to get access to getSystemService: https://stackoverflow.com/questions/49007114/testing-android-app-trying-to-mock-getsystemservice
    This too: https://developer.android.com/training/testing/local-tests
    We'll use an Interface LocationProvider to provide implementation for DatabaseExampleActivity
    Once it's all setup, we'll spoof a location via LocationManager and with JUnit tests just check if that function works properly
    Then we'll copy the code over with a real location into the AndroidLocationProvider

    Location Permissions are granted in manifest, still need to fix Mockito stuff in gradle
     */


}