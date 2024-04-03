package dal.cs.quickcash3.employer;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static dal.cs.quickcash3.test.RegexMatcher.withPattern;
import static dal.cs.quickcash3.test.WaitForAction.waitFor;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dal.cs.quickcash3.R;

@RunWith(AndroidJUnit4.class)
public class EmployerDashLocationEspressoTest {
    @Rule
    public final ActivityScenarioRule<EmployerDashLocation> activityRule =
        new ActivityScenarioRule<>(EmployerDashLocation.class);
    @Rule
    public GrantPermissionRule grantLocationPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    private static final int MAX_LOCATION_TIMEOUT_MS = 30000;

    @Test
    public void checkIfElementsVisible() {
        Espresso.onView(withId(R.id.addressText)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.detectLocationButton)).check(matches(isDisplayed()));
    }

    @Test
    public void checkAddressDisplayed() {
        Espresso.onView(withId(R.id.detectLocationButton)).perform(click());
        Espresso.onView(withId(R.id.addressText)).perform(waitFor(withPattern("Address: [a-zA-Z0-9 .,]+"), MAX_LOCATION_TIMEOUT_MS));
    }
}