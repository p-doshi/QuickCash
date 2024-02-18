package com.example.csci3130_group_3;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * UI Automator is required because we need to detect permission popups
 * UI Tests for User Story 7: Location
 */
@RunWith(AndroidJUnit4.class)
public class LocationUITests {
    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackage = "com.example.csci3130_group_3";
    private UiDevice device;
    private int sdkVersion;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent appIntent = new Intent(context, LocationExampleActivity.class);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(appIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        sdkVersion = Build.VERSION.SDK_INT;
    }

    // Basic preliminary test to check if UI elements spawned correctly
    @Test
    public void checkIfElementsVisible() {
        assertViewWithTextVisible(device, "Location Permission");
        assertViewWithTextVisible(device, "Detect Location");
        assertViewWithTextVisible(device, "Longitude");
        assertViewWithTextVisible(device, "Latitude");
    }


    // Helper method, checks if there's a View containing the text, if not throws assertion error
    public static void assertViewWithTextVisible(UiDevice device, String text) {
        UiObject allowButton = device.findObject(new UiSelector().textContains(text));
        if (!allowButton.exists()) {
            throw new AssertionError("View with text: \""+text+"\" not found!");
        }
    }

    // Helper method, denies the current permission popup.
    public static void denyCurrentPermission(UiDevice device) throws UiObjectNotFoundException {
        UiObject denyButton = device.findObject(new UiSelector().text("Deny"));
        denyButton.click();
    }

    // Helper method, specifically denies the location request popup
    public static void denyCurrentLocationRequest(UiDevice device) throws UiObjectNotFoundException {
        UiObject nothanksButton = device.findObject(new UiSelector().text("No, thanks"));
        nothanksButton.click();
    }

    @Test
    public void a_testDisplayedLocationPermissionRequest() throws Exception {
        UiObject requestLocationButton = device.findObject(new UiSelector().textContains("Detect Location"));
        requestLocationButton.click();
        // In android version 11 and beyond the request location permissions popup was changed
        // So this checks which version they're running before asserting what it should see
        Log.d("LocationTests", "SDK: "+sdkVersion+" Location Permissions Test Running");
        if (sdkVersion >= Build.VERSION_CODES.R) {
            assertViewWithTextVisible(device, "While using the app");
        } else {
            assertViewWithTextVisible(device, "ALLOW");
        }

        // Clean up for next test
        denyCurrentPermission(device);
        denyCurrentLocationRequest(device);
    }
}
