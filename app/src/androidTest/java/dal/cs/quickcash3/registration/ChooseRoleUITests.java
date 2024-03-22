package dal.cs.quickcash3.registration;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.Manifest;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChooseRoleUITests {
    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public final ActivityScenarioRule<ChooseRoleActivity> activityRule =
        new ActivityScenarioRule<>(ChooseRoleActivity.class);
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());

    @Test
    public void checkIfUiVisible() {
        UiObject workerBox = device.findObject(new UiSelector().text("Worker"));
        assertTrue(workerBox.exists());
        UiObject employerBox = device.findObject(new UiSelector().textContains("Employer"));
        assertTrue(employerBox.exists());
        UiObject confirmBox = device.findObject(new UiSelector().textContains("Confirm"));
        assertTrue(confirmBox.exists());
    }
    @Test
    public void checkIfMovedToEmployerDashboard() throws UiObjectNotFoundException {
        UiObject employerButton = device.findObject(new UiSelector().textContains("Employer"));
        employerButton.click();
        UiObject confirmButton = device.findObject(new UiSelector().textContains("Confirm"));
        confirmButton.clickAndWaitForNewWindow();
        UiObject employerDashLabel = device.findObject(new UiSelector().textContains("Current Listings"));
        assertTrue(employerDashLabel.exists());
    }
    @Test
    public void checkIfMovedToWorkerDashboard() throws UiObjectNotFoundException {
        UiObject workerButton = device.findObject(new UiSelector().textContains("Worker"));
        workerButton.click();
        UiObject confirmButton = device.findObject(new UiSelector().textContains("Confirm"));
        confirmButton.clickAndWaitForNewWindow();
        UiObject workerDashLabel = device.findObject(new UiSelector().textContains("Map"));
        assertTrue(workerDashLabel.exists());
    }
}
