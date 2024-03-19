package dal.cs.quickcash3.worker;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertTrue;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;

import dal.cs.quickcash3.location.WorkerDashboardMapExampleActivity;

@RunWith(AndroidJUnit4.class)
public class WorkerDashMapsUITest {
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
        UiObject Me=device.findObject(new UiSelector().descriptionContains("Me"));
        Me.click();
        assertTrue(Me.exists());
    }

    @Test
    public void testMapWorker1IsDisplayed() throws UiObjectNotFoundException{
        UiObject Worker1=device.findObject(new UiSelector().descriptionContains("Job 1"));
        Worker1.click();
        assertTrue(Worker1.exists());
    }

    @Test
    public void testMapWorker2IsDisplayed() throws UiObjectNotFoundException{
        UiObject Worker2=device.findObject(new UiSelector().descriptionContains("Job 2"));
        Worker2.click();
        assertTrue(Worker2.exists());
    }
}
