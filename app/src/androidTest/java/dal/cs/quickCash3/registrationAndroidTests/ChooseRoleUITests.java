package dal.cs.quickCash3.registrationAndroidTests;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

import android.content.Intent;


import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import dal.cs.quickCash3.registration.ChooseRoleActivity;

import org.junit.Before;

/**
 * Tests for user story 3 - Choose Roles
 */

@RunWith(AndroidJUnit4.class)
public class ChooseRoleUITests {

    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackage = "dal.cs.quickCash3";
    private UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        final Intent appIntent = new Intent(context, ChooseRoleActivity.class);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(appIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkIfUiVisible() {
        UiObject employeeBox = device.findObject(new UiSelector().text("Employee"));
        assertTrue(employeeBox.exists());
        UiObject employerBox = device.findObject(new UiSelector().textContains("Employer"));
        assertTrue(employerBox.exists());
        UiObject confirmBox = device.findObject(new UiSelector().textContains("Confirm"));
        assertTrue(confirmBox.exists());
    }
    @Test
    @Ignore("only after pages connected")
    public void checkIfMovedToEmployerDashboard() throws UiObjectNotFoundException {
        UiObject employerButton = device.findObject(new UiSelector().textContains("Employer"));
        employerButton.click();
        UiObject confirmButton = device.findObject(new UiSelector().textContains("Confirm"));
        confirmButton.clickAndWaitForNewWindow();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains("Welcome"));
        assertTrue(welcomeLabel.exists());
    }
    @Test
    @Ignore("only after pages connected")
    public void checkIfMovedToEmployeeDashboard() throws UiObjectNotFoundException {
        UiObject employeeButton = device.findObject(new UiSelector().textContains("Employee"));
        employeeButton.click();
        UiObject confirmButton = device.findObject(new UiSelector().textContains("Confirm"));
        confirmButton.clickAndWaitForNewWindow();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains("Welcome"));
        assertTrue(welcomeLabel.exists());
    }
}
