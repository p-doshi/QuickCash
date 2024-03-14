package dal.cs.quickcash3.employer;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.Manifest;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.rule.GrantPermissionRule;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.util.RegexMatcher.withPattern;
import static dal.cs.quickcash3.util.WaitForAction.waitFor;

import android.content.Context;
import android.content.Intent;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import org.junit.Before;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.employer.EmployerDashLocation;
import dal.cs.quickcash3.location.*;

@RunWith(AndroidJUnit4.class)
public class EmployerDashLocationEspressoTest {

    public ActivityScenario<EmployerDashLocation> scenario;
    public Context context;
    @Rule
    public GrantPermissionRule grantLocationPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    public int MAX_LOCATION_TIMEOUT_MS = 10000;
    @Before
    public void setup() {
        scenario = ActivityScenario.launch(EmployerDashLocation.class);
        scenario.onActivity(activity -> {
            context = activity;
        });
    }

    @Test
    public void checkIfElementsVisible() {
        Espresso.onView(withId(R.id.addressText)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.detectLocationButton)).check(matches(isDisplayed()));
    }

    @Test
    public void checkAddressDisplayed() {
        // Clicks the button once
        Espresso.onView(withId(R.id.detectLocationButton)).perform(click());
        // Address will be printed in the format of 1600 Amphitheatre Parkway, Mountain View, CA 94043, USA need to adjust the regex to detect this
        Espresso.onView(withId(R.id.addressText)).perform(waitFor(withPattern("Address: [0-9]+ [a-zA-Z .]+, [a-zA-Z ]+, [A-Z0-9 ]+,[a-zA-Z ]+"), MAX_LOCATION_TIMEOUT_MS));
    }
}