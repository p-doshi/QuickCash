package dal.cs.quickcash3.payment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.COMPLETED_JOB1_PAY_ID;
import static dal.cs.quickcash3.test.ExampleJobList.generateCompletedJobs;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.test.ActivityMonitorRule;

@RunWith(AndroidJUnit4.class)
public class WorkerPaymentEspressoTests {
    private final Context context = ApplicationProvider.getApplicationContext();
    @Rule
    public final ActivityScenarioRule<WorkerPayPalActivity> activityRule =
        new ActivityScenarioRule<>(
            new Intent(context, WorkerPayPalActivity.class)
                .addCategory(context.getString(R.string.MOCK_DATABASE)));
    @Rule
    public final ActivityMonitorRule<WorkerPaymentConfirmationActivity> monitorRule =
        new ActivityMonitorRule<>(WorkerPaymentConfirmationActivity.class);
    private static final int MAX_TIMEOUT = 15000;
    private Database database;

    @Before
    public void setup() {
        ActivityScenario<WorkerPayPalActivity> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Do not run the test if we are not using the mock database.
            assertTrue("Not using Mock Database", activity.getDatabase() instanceof MockDatabase);
            database = activity.getDatabase();
        });

        generateCompletedJobs(database, Assert::fail);
    }

    @Test
    public void showPayConfirmationButton(){
        onView(withId(R.id.seePayStatus)).check(matches(isDisplayed()));
    }

    @Test
    public void showPaymentStatus() {
        onView(withId(R.id.seePayStatus)).perform(click());
        monitorRule.waitForActivity(MAX_TIMEOUT);
        onView(withId(R.id.workerPayID)).check(matches(withText(COMPLETED_JOB1_PAY_ID)));
    }
}
