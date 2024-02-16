package com.example.csci3130_group_3;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

// Unit tests can debug the logic
// So they're the best choice for checking if location is enabled
public class LocationUnitTest {

    // Mock all the functions for context, activity & locationProviderClient
    @Mock
    private Context mockContext;
    @Mock
    private Activity mockActivity;
    @Mock
    private FusedLocationProviderClient mockLocationProviderClient;
    @Mock
    private AndroidLocationProvider locationProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        locationProvider = new AndroidLocationProvider(mockContext, mockActivity);
    }

    @Test
    public void testPermsGranted() {
        // Assumes location permission is granted
        when(mockContext.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION))
                .thenReturn(PackageManager.PERMISSION_GRANTED);
        when(mockContext.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION))
                .thenReturn(PackageManager.PERMISSION_GRANTED);

        assertTrue(locationProvider.checkLocationPermissionsEnabled());
    }

    /*@Test
    public void testLocationPermissionEnabled();
    @Test
    public void testLocationPermissionDisabled();
    @Test
    public void getLocation() {}*/



    /* So here's the jist.
    We need to use mockito to get access to getSystemService: https://stackoverflow.com/questions/49007114/testing-android-app-trying-to-mock-getsystemservice
    This too: https://developer.android.com/training/testing/local-tests
    We'll use an Interface LocationProvider to provide implementation for DatabaseExampleActivity
    Once it's all setup, we'll spoof a location via LocationManager and with JUnit tests just check if that function works properly
    Then we'll copy the code over with a real location into the AndroidLocationProvider

    Location Permissions are granted in manifest, still need to fix Mockito stuff in gradle
     */


}