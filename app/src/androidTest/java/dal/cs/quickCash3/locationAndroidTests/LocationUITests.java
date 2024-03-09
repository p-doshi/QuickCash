package dal.cs.quickCash3.locationAndroidTests;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import dal.cs.quickCash3.location.LocationExampleActivity;

/**
 * UI Automator is required because we need to detect permission popups
 * UI Tests for User Story 7: Location
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals") // Seems unnecessarily picky here.
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocationUITests {
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String LAUNCHER_PACKAGE = "dal.cs.quickCash33";
    private UiDevice device;
    private Context context;
    private int sdkVersion;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());
        context = ApplicationProvider.getApplicationContext();
        Intent appIntent = new Intent(context, LocationExampleActivity.class);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(appIntent);
        device.wait(Until.hasObject(By.pkg(LAUNCHER_PACKAGE).depth(0)), LAUNCH_TIMEOUT);

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

    // Helper method, clicks any buttons (Usually permission popups) given their text
    public static void clickVisibleButton(UiDevice device, String text) throws UiObjectNotFoundException {
        UiObject acceptButton = device.findObject(new UiSelector().textContains(text));
        acceptButton.click();
    }

    // Helper method, denies the current permission popup if it shows
    private void denyCurrentPermission(UiDevice device) throws UiObjectNotFoundException {
        UiObject denyButton;
        if (sdkVersion >= Build.VERSION_CODES.R) { // NOPMD LawOfDemeter is excessive here.
            denyButton = device.findObject(new UiSelector().textContains("Don"));
        } else {
            denyButton = device.findObject(new UiSelector().text("Deny"));
        }
        denyButton.click();
    }

    // Helper method, denies the location setting request popup if it shows
    private static void denyCurrentLocationSettingRequest(UiDevice device) throws UiObjectNotFoundException {
        UiObject nothanksButton = device.findObject(new UiSelector().text("No, thanks"));
        nothanksButton.click();
    }

    // Helper method, checks if Location Permissions have been granted to the app
    private boolean checkLocationPermissionsEnabled() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Helper method, checks if Location Setting is enabled or disabled on device
    private boolean checkLocationSettingEnabled() throws Settings.SettingNotFoundException {
        int locationSetting = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        return locationSetting == Settings.Secure.LOCATION_MODE_OFF; // NOPMD LawOfDemeter is excessive here.
    }

    @Test
    public void testDisplayedLocationPermissionRequest() throws Exception {
        UiObject requestLocationButton = device.findObject(new UiSelector().textContains("Detect Location"));
        requestLocationButton.click();
        // In android version 11 and beyond the request location permissions popup was changed
        // So this checks which version they're running before asserting what it should see
        Log.d("LocationTests", "SDK: "+sdkVersion+" Location Permissions Test Running");
        // First checks if location permissions are already granted, if so then just check if granted works
        if (!checkLocationPermissionsEnabled()) {
            if (sdkVersion >= Build.VERSION_CODES.R) { // NOPMD LawOfDemeter is excessive here.
                assertViewWithTextVisible(device, "access this device");
                assertViewWithTextVisible(device,"Don");
            } else {
                assertViewWithTextVisible(device, "ALLOW");
                assertViewWithTextVisible(device, "DENY");
            }
        } else {
            assertViewWithTextVisible(device, "Granted");
        }

        // Clean up for next test
        if (!checkLocationPermissionsEnabled()) {
            denyCurrentPermission(device);
        }
        if (checkLocationSettingEnabled()) {
            denyCurrentLocationSettingRequest(device);
        }
    }

    @Test
    public void denyLocationSettingsAndPermissions() throws UiObjectNotFoundException, Settings.SettingNotFoundException {
        UiObject requestLocationButton = device.findObject(new UiSelector().textContains("Detect Location"));
        requestLocationButton.click();
        Log.d("LocationTests", "SDK: "+sdkVersion+" Denying Location Setting/Permission Test Running");
        denyCurrentPermission(device);
        if (checkLocationSettingEnabled()) {
            denyCurrentLocationSettingRequest(device);
        }
        assertViewWithTextVisible(device, "Not Granted");
    }

    @Test
    public void acceptLocationPermissions() throws UiObjectNotFoundException, Settings.SettingNotFoundException {
        UiObject requestLocationButton = device.findObject(new UiSelector().textContains("Detect Location"));
        requestLocationButton.click();
        Log.d("LocationTests", "SDK: "+sdkVersion+" Accepting Location Setting/Permission Test Running");
        if (!checkLocationPermissionsEnabled()) {
            if (sdkVersion >= Build.VERSION_CODES.R) { // NOPMD LawOfDemeter is excessive here.
                assertViewWithTextVisible(device, "Only this time");
                clickVisibleButton(device, "Only this time");
            } else {
                assertViewWithTextVisible(device, "Allow");
                clickVisibleButton(device, "Allow");
            }
        }
        if (checkLocationSettingEnabled()) {
            assertViewWithTextVisible(device,"No, thanks");
            assertViewWithTextVisible(device, "OK");
            clickVisibleButton(device, "OK");
        }

        requestLocationButton.click();
        assertViewWithTextVisible(device, "Granted");
        assertViewWithTextVisible(device, "PENDING");
    }
}
