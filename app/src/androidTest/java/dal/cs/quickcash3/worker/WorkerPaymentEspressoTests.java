package dal.cs.quickcash3.worker;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.payment.WorkerPayPalActivity;

import org.junit.Before;
import org.junit.Test;

public class WorkerPaymentEspressoTests {

    public ActivityScenario<WorkerPayPalActivity> scenario;
    private UiDevice device;


    @Before
    public void setup() {
        scenario = ActivityScenario.launch(WorkerPayPalActivity.class);
        device = UiDevice.getInstance(getInstrumentation());
        scenario.onActivity(activity -> {

        });
    }

    @Test
    public void showPayConfirmationButton(){
        onView(ViewMatchers.withId(R.id.seePayStatus)).perform(click());
    }

    @Test
    public void moveToPayConfirmationPage() {
        onView(ViewMatchers.withId(R.id.seePayStatus)).perform(click());
        onView(withId(R.id.workerStatusTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void showPaymentStatus(){
        onView(ViewMatchers.withId(R.id.seePayStatus)).perform(click());
//        UiObject statusMessage = device.findObject(R.id.statusMessage);
//        assertTrue(statusMessage.exists());
//        UiObject payID = device.findObject(R.id.payID);
//        assertTrue(payID.exists());
    }

}
