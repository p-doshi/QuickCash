package dal.cs.quickcash3.worker;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.Manifest;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dal.cs.quickcash3.location.WorkerDashboardMapExampleActivity;

@RunWith(AndroidJUnit4.class)
public class WorkerDashMapsUITest {
    private static final int MAX_LOAD_TIMEOUT = 30000;
    @Rule
    public final ActivityScenarioRule<WorkerDashboardMapExampleActivity> activityRule =
        new ActivityScenarioRule<>(WorkerDashboardMapExampleActivity.class);
    @Rule
    public GrantPermissionRule permissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());
    private final String appPackage = ApplicationProvider.getApplicationContext().getPackageName();

    @Before
    public void setup() throws UiObjectNotFoundException {
        device.findObject(new UiSelector().resourceId(appPackage + ":id/workerMapPage")).click();
    }

    @Test
    public void testMapMeIsDisplayed() throws UiObjectNotFoundException {
        UiObject myMarker = device.findObject(new UiSelector().descriptionContains("Me"));
        assertTrue(myMarker.waitForExists(MAX_LOAD_TIMEOUT));
        myMarker.click();
    }

    @Test
    public void testMapWorker1IsDisplayed() throws UiObjectNotFoundException{
        UiObject jobMarker = device.findObject(new UiSelector().descriptionContains("Job 1"));
        assertTrue(jobMarker.waitForExists(MAX_LOAD_TIMEOUT));
        jobMarker.click();
    }

    @Test
    public void testMapWorker2IsDisplayed() throws UiObjectNotFoundException{
        UiObject jobMarker = device.findObject(new UiSelector().descriptionContains("Job 2"));
        assertTrue(jobMarker.waitForExists(MAX_LOAD_TIMEOUT));
        jobMarker.click();
    }
}
