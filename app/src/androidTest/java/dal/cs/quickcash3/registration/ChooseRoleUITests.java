package dal.cs.quickcash3.registration;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
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
    public final ActivityScenarioRule<ChooseRoleActivity> activityRule =
        new ActivityScenarioRule<>(ChooseRoleActivity.class);
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());

    @Test
    public void checkIfUiVisible() {
        UiObject workerBox = device.findObject(new UiSelector().text("worker"));
        assertTrue(workerBox.exists());
        UiObject employerBox = device.findObject(new UiSelector().textContains("employer"));
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
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains("Current Listings"));
        assertTrue(welcomeLabel.exists());
    }
    @Test
    @Ignore("only after pages connected")
    public void checkIfMovedToWorkerDashboard() throws UiObjectNotFoundException {
        UiObject workerButton = device.findObject(new UiSelector().textContains("Worker"));
        workerButton.click();
        UiObject confirmButton = device.findObject(new UiSelector().textContains("Confirm"));
        confirmButton.clickAndWaitForNewWindow();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains("Jobs"));
        assertTrue(welcomeLabel.exists());
    }
}
