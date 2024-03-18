package dal.cs.quickcash3.worker;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

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
    UiDevice device;
    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackage = "dal.cs.quickcash3";
    private final int SDK_VERSION = Build.VERSION.SDK_INT;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());

        Context context = ApplicationProvider.getApplicationContext();
        Intent launcherIntent = new Intent(context, WorkerDashboardMapExampleActivity.class);

        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launcherIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // This just dismisses the allow permission popup
        try {
            allowPermissions();
        } catch (UiObjectNotFoundException e) {
            // If we can't find the allow permission that's fine, it means it was already granted
        }

        try {
            UiObject mapTab=device.findObject(new UiSelector().descriptionContains("Map"));
            mapTab.click();
        } catch (UiObjectNotFoundException e) {
            // The map will always be found so its fine
        }
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
    private void allowPermissions() throws UiObjectNotFoundException {
        String allowRegex;
        switch (SDK_VERSION) {
            case 28: allowRegex = "ALLOW"; break;
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
    }
}
