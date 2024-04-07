package dal.cs.quickcash3.worker;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.generateCompletedJobs;
import static dal.cs.quickcash3.test.ExampleUserList.WORKER1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.payment.PaymentConfirmationActivity;
import dal.cs.quickcash3.test.ActivityMonitorRule;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") // This increases code readability.
@RunWith(AndroidJUnit4.class)
public class WorkerPaymentEspressoTests {
    private final Context context = ApplicationProvider.getApplicationContext();
    @Rule
    public final ActivityScenarioRule<WorkerDashboard> activityRule =
            new ActivityScenarioRule<>(
                    new Intent(context, WorkerDashboard.class)
                            .addCategory(context.getString(R.string.MOCK_DATABASE))
                            .putExtra("user", WORKER1));
    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public ActivityMonitorRule<PaymentConfirmationActivity> monitorRule =
            new ActivityMonitorRule<>(PaymentConfirmationActivity.class);
    private Database database;
    private static final int MAX_TIMEOUT = 30000;

    @Before
    public void setup() throws Throwable {
        ActivityScenario<WorkerDashboard> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Do not run the test if we are not using the mock implementation.
            assertTrue("Not using Mock Database",
                    activity.getDatabase() instanceof MockDatabase);

            database = activity.getDatabase();
        });

        // Navigate to the history tab.
        onView(withId(R.id.workerHistoryPage)).perform(click());

        runOnUiThread(() -> generateCompletedJobs(database, Assert::fail));
    }

    @Test
    public void viewPaymentStatus() {
        onView(allOf(withId(R.id.title), withText("Painter"))).perform(scrollTo(), click());

        onView(withId(R.id.checkPayStatus)).perform(click());
        monitorRule.waitForActivity(MAX_TIMEOUT);
        onView(withId(R.id.workerStatusTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.payID)).check(matches(isDisplayed()));
    }
}
