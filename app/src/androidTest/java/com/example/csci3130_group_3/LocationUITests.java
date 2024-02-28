package com.example.csci3130_group_3;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.Manifest;
import android.app.UiAutomation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LocationUITests {
    private final int SDK_VERSION = Build.VERSION.SDK_INT;
    private Context context;
    private UiAutomation automation;
    private UiDevice device;
    private String launcherPackage;

    private void disablePermission(String permission) {
        int code = context.checkSelfPermission(permission);
        if (code == PackageManager.PERMISSION_GRANTED) {
            automation.revokeRuntimePermission(launcherPackage, permission);
        }
    }

    @Before
    public void setup() {
        context = getInstrumentation().getContext();
        automation = getInstrumentation().getUiAutomation();
        device = UiDevice.getInstance(getInstrumentation());
        launcherPackage = device.getLauncherPackageName();

        // Remove location permissions before starting.
        disablePermission(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario.launch(LocationExampleActivity.class);
    }

    private void pressOK() throws UiObjectNotFoundException {
        UiObject noThanksButton = device.findObject(new UiSelector().text("OK").clickable(true));
        if (noThanksButton.exists()) {
            noThanksButton.click();
        }
    }

    // Helper method, denies the current permission popup if it shows.
    private void denyPermissions() throws UiObjectNotFoundException {
        UiObject denyButton;
        if (SDK_VERSION >= Build.VERSION_CODES.R) {
            denyButton = device.findObject(new UiSelector().textMatches("Don.t allow"));
        } else {
            denyButton = device.findObject(new UiSelector().text("DENY"));
        }
        denyButton.click();

        pressOK();
    }

    // Helper method, denies the current permission popup if it shows.
    private void allowPermissions() throws UiObjectNotFoundException {
        UiObject allowButton;
        if (SDK_VERSION >= Build.VERSION_CODES.R) {
            allowButton = device.findObject(new UiSelector().text("Only this time"));
        } else {
            allowButton = device.findObject(new UiSelector().text("ALLOW"));
        }
        allowButton.click();

        pressOK();
    }

    @Test
    public void denyLocationPermissions() throws UiObjectNotFoundException {
        UiObject requestLocationButton = device.findObject(new UiSelector().textContains("Detect Location").clickable(true));
        requestLocationButton.click();
        denyPermissions();
        assertTrue(device.findObject(new UiSelector().textContains("Not Granted")).exists());
    }

    @Test
    public void allowLocationPermissions() throws UiObjectNotFoundException {
        UiObject requestLocationButton = device.findObject(new UiSelector().textContains("Detect Location").clickable(true));
        requestLocationButton.click();
        allowPermissions();
        assertTrue(device.findObject(new UiSelector().textContains("Granted")).exists());
    }

    @Test
    public void getLocation() throws UiObjectNotFoundException {
        UiObject requestLocationButton = device.findObject(new UiSelector().textContains("Detect Location").clickable(true));
        requestLocationButton.click();
        allowPermissions();

        // Press the button again to get the location.
        requestLocationButton.click();

        assertTrue(device.findObject(new UiSelector().textMatches("Longitude: [-\\d.]+")).exists());
        assertTrue(device.findObject(new UiSelector().textMatches("Latitude: [-\\d.]+")).exists());
    }
}
