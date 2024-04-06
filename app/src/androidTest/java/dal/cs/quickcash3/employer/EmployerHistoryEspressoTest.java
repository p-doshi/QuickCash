package dal.cs.quickcash3.employer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.AVAILABLE_JOBS;
import static dal.cs.quickcash3.test.ExampleJobList.generateCompletedJobs;
import static dal.cs.quickcash3.test.ExampleUserList.EMPLOYER1;
import static dal.cs.quickcash3.test.ExampleUserList.WORKER1;
import static dal.cs.quickcash3.test.ExampleUserList.WORKER2;
import static dal.cs.quickcash3.test.RecyclerViewItemCountMatcher.recyclerHasItemCount;
import static dal.cs.quickcash3.test.SiblingMatcher.withSibling;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") // This increases code readability.
@RunWith(AndroidJUnit4.class)
public class EmployerHistoryEspressoTest {
    private final Context context = ApplicationProvider.getApplicationContext();
    @Rule
    public final ActivityScenarioRule<EmployerDashboard> activityRule =
        new ActivityScenarioRule<>(
            new Intent(context, EmployerDashboard.class)
                .addCategory(context.getString(R.string.MOCK_DATABASE))
                .putExtra("user", EMPLOYER1));
    private Database database;

    private void checkJobPosts(@NonNull List<String> expectedJobTitles) {
        onView(withId(R.id.jobListRecyclerView)).check(matches(recyclerHasItemCount(expectedJobTitles.size())));

        for (AvailableJob job : AVAILABLE_JOBS) {
            if (expectedJobTitles.contains(job.getTitle())) {
                onView(allOf(withId(R.id.title), withText(job.getTitle())));
                onView(allOf(withId(R.id.subhead), withText(job.getDescription())));
            }
        }
    }

    @Before
    public void setup() throws Throwable {
        ActivityScenario<EmployerDashboard> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Do not run the test if we are not using the mock implementation.
            assertTrue("Not using Mock Database", activity.getDatabase() instanceof MockDatabase);

            database = activity.getDatabase();
        });

        // Navigate to the history tab.
        onView(withId(R.id.employer_history)).perform(click());

        runOnUiThread(() -> generateCompletedJobs(database, Assert::fail));
    }

    @Test
    public void checkHistory() {
        List<String> expectedJobTitles = Arrays.asList(
            "House Cleaner",
            "Gardener"
        );

        checkJobPosts(expectedJobTitles);
    }

    @Test
    public void viewJobDetails() {
        onView(allOf(withId(R.id.title), withText("House Cleaner"))).perform(scrollTo(), click());

        onView(withId(R.id.jobTitle)).check(matches(withText("House Cleaner")));
        onView(withId(R.id.jobAddress)).check(matches(isDisplayed()));
        onView(withId(R.id.jobPay)).check(matches(isDisplayed()));
        onView(withId(R.id.jobDescription)).check(matches(isDisplayed()));
    }
}
