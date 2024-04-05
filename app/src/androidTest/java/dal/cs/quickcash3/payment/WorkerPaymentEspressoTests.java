package dal.cs.quickcash3.payment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.test.ActivityMonitorRule;

@RunWith(AndroidJUnit4.class)
public class WorkerPaymentEspressoTests {
    @Rule
    public final ActivityScenarioRule<WorkerPayPalActivity> activityRule =
        new ActivityScenarioRule<>(WorkerPayPalActivity.class);
    @Rule
    public final ActivityMonitorRule<WorkerPaymentConfirmationActivity> monitorRule =
        new ActivityMonitorRule<>(WorkerPaymentConfirmationActivity.class);
    private static final int MAX_TIMEOUT = 15000;

    @Test
    public void showPayConfirmationButton(){
        onView(withId(R.id.seePayStatus)).check(matches(isDisplayed()));
    }

    @Test
    public void showPaymentStatus() {
        onView(withId(R.id.seePayStatus)).perform(click());
        monitorRule.waitForActivity(MAX_TIMEOUT);
        onView(withId(R.id.workerStatusMessage)).check(matches(withText("approved")));
        onView(withId(R.id.workerPayID)).check(matches(withText("Hello Yuki")));
    }
}
