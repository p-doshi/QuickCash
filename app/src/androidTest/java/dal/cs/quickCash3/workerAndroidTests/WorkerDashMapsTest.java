package dal.cs.quickCash3.workerAndroidTests;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;

import dal.cs.quickCash3.location.WorkerDashMapsTestActivity;

@RunWith(AndroidJUnit4.class)
public class WorkerDashMapsTest {
    UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());

        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent launcherIntent = new Intent(context, WorkerDashMapsTestActivity.class);

        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launcherIntent);
    }

    @Test
    public void testMapMeIsDisplayed() throws UiObjectNotFoundException {
        UiObject Me=device.findObject(new UiSelector().descriptionContains("Me"));
        Me.click();
        assertTrue(Me.exists());
    }

    @Test
    public void testMapWorker1IsDisplayed() throws UiObjectNotFoundException{
        UiObject Worker1=device.findObject(new UiSelector().descriptionContains("Worker1"));
        Worker1.click();
        assertTrue(Worker1.exists());
    }

    @Test
    public void testMapWorker2IsDisplayed() throws UiObjectNotFoundException{
        UiObject Worker2=device.findObject(new UiSelector().descriptionContains("Worker2"));
        Worker2.click();
        assertTrue(Worker2.exists());
    }
}
