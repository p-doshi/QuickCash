package com.example.csci3130_group_3;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.os.Build;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class LocationUITests {
    private static final int SDK_VERSION = Build.VERSION.SDK_INT;
    @Rule
    public final ActivityScenarioRule<LocationExampleActivity> activity =
        new ActivityScenarioRule<>(LocationExampleActivity.class);
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());

    private void pressOkIfExists() throws UiObjectNotFoundException {
        UiObject noThanksButton = device.findObject(new UiSelector().text("OK").clickable(true));
        if (noThanksButton.exists()) {
            noThanksButton.click();
        }
    }

    private void denyPermissions() throws UiObjectNotFoundException {
        String denyRegex;
        switch (SDK_VERSION) {
            case 27: denyRegex = "DENY"; break;
            case 28:
            case 29:
            case 30: denyRegex = "Deny"; break;
            case 31:
            case 32:
            case 33:
            case 34: denyRegex = "Don.t allow"; break;
            default: denyRegex = ""; break;
        }

        UiObject denyButton = device.findObject(new UiSelector().textMatches(denyRegex));
        denyButton.click();

        pressOkIfExists();
    }

    private void allowPermissions() throws UiObjectNotFoundException {
        String allowRegex;
        switch (SDK_VERSION) {
            case 28: allowRegex = "Allow"; break;
            case 29: allowRegex = "Allow only while using the app"; break;
            case 30:
            case 31:
            case 32:
            case 33:
            case 34: allowRegex = "Only this time"; break;
            default: allowRegex = ""; break;
        }

        UiObject allowButton = device.findObject(new UiSelector().textMatches(allowRegex));
        allowButton.click();

        pressOkIfExists();
    }

    @Test
    public void a_denyLocationPermissions() throws UiObjectNotFoundException {
        UiObject requestLocationButton = device.findObject(new UiSelector().textContains("Detect Location").clickable(true));
        requestLocationButton.click();
        denyPermissions();
        assertTrue(device.findObject(new UiSelector().textContains("Not Granted")).exists());
    }

    @Test
    public void b_allowLocationPermissions() throws UiObjectNotFoundException {
        UiObject requestLocationButton = device.findObject(new UiSelector().textContains("Detect Location").clickable(true));
        requestLocationButton.click();
        allowPermissions();
        assertTrue(device.findObject(new UiSelector().textContains("Granted")).exists());
    }

    @Test
    public void c_getLocation() throws UiObjectNotFoundException, InterruptedException {
        UiObject requestLocationButton = device.findObject(new UiSelector().textContains("Detect Location").clickable(true));
        requestLocationButton.click();

        // New Code inserted by Mathew
        // Sets the test to wait for location to update, this is unavoidable as android is slow
        final int waitLocationDelay = 5000;
        Thread.sleep(waitLocationDelay);
        requestLocationButton.click();

        assertTrue(device.findObject(new UiSelector().textMatches("Longitude: [-\\d.]+")).exists());
        assertTrue(device.findObject(new UiSelector().textMatches("Latitude: [-\\d.]+")).exists());
    }
}
