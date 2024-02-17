package com.example.csci3130_group_3;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.lang.reflect.Method;
import java.util.List;


/**
 * UI Automator is required because we need to detect permission popups
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocationUITests {
    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackage = "com.example.csci3130_group_3";
    private UiDevice device;
    private Context context;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());
        context = ApplicationProvider.getApplicationContext();
        final Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackage);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(appIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
    }

    // Helper method, checks if there's a permissions popup if not throws assertion error
    public static void assertViewWithTextVisible(UiDevice device, String text) {
        UiObject allowButton = device.findObject(new UiSelector().text(text));
        if (!allowButton.exists()) {
            throw new AssertionError("View with text: <"+text+"> not found!");
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
        assertViewWithTextVisible(device, "ALLOW");
        assertViewWithTextVisible(device, "DENY");

        // Clean up for next test
        denyCurrentPermission(device);
        denyCurrentLocationRequest(device);
    }
}
