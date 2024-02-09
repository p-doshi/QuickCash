package com.example.csci3130_group_3;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

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

//need to change the name according to your file
//import com.example.csci3130_group_3.EmployeeDashboard;

public class EmployeeDashboardUITest {
    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackage = "com.example.csci3130_group_3";
    private UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        final Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackage);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(appIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkIfEmployeeDashboardPageIsVisible() {
        UiObject employeeMapIcon = device.findObject(new UiSelector().resourceId("employeeMapPage"));
        assertTrue(employeeMapIcon.exists());
        UiObject employeeSearchIcon = device.findObject(new UiSelector().resourceId("employeeSearchPage"));
        assertTrue(employeeSearchIcon.exists());
        UiObject employeeReceiptIcon = device.findObject(new UiSelector().resourceId("employeeReceiptPage"));
        assertTrue(employeeReceiptIcon.exists());
        UiObject employeeProfileIcon = device.findObject(new UiSelector().resourceId("employeeProfilePage"));
        assertTrue(employeeProfileIcon.exists());
    }
}
