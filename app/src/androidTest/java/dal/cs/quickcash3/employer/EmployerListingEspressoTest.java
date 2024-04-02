package dal.cs.quickcash3.employer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.JOBS;
import static dal.cs.quickcash3.test.ExampleJobList.USER;
import static dal.cs.quickcash3.test.ExampleJobList.generateJobPosts;
import static dal.cs.quickcash3.test.RecyclerViewItemCountMatcher.recyclerHasItemCount;

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

import java.util.Arrays;
import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;

@RunWith(AndroidJUnit4.class)
public class EmployerListingEspressoTest {
    private final Context context = ApplicationProvider.getApplicationContext();
    @Rule
    public final ActivityScenarioRule<EmployerDashboard> activityRule =
        new ActivityScenarioRule<>(
            new Intent(context, EmployerDashboard.class)
                .addCategory(context.getString(R.string.MOCK_DATABASE))
                .putExtra("user", USER));

    private Database database;

    private void checkJobPosts(@NonNull List<String> expectedJobTitles) {
        onView(withId(R.id.jobListRecyclerView)).check(matches(recyclerHasItemCount(expectedJobTitles.size())));

        for (AvailableJob job : JOBS.values()) {
            if (expectedJobTitles.contains(job.getTitle())) {
                onView(allOf(withId(R.id.title), withText(job.getTitle())));
                onView(allOf(withId(R.id.subhead), withText(job.getDescription())));
            }
        }
    }

    @Before
    public void setup() {
        ActivityScenario<EmployerDashboard> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Do not run the test if we are not using the mock implementation.
            assertTrue("Not using Mock Database", activity.getDatabase() instanceof MockDatabase);

            database = activity.getDatabase();
        });
    }

    @Test
    public void restrictedList() throws Throwable {
        runOnUiThread(() -> generateJobPosts(database, Assert::fail));

        List<String> expectedJobTitles = Arrays.asList(
            "Walk Dog",
            "Coding problem"
        );

        checkJobPosts(expectedJobTitles);
    }
}
