package dal.cs.quickcash3.search;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.generateAvailableJobs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class SearchJobEspressoTest {
    private final Context context = ApplicationProvider.getApplicationContext();
    @Rule
    public final ActivityScenarioRule<WorkerDashboard> activityRule =
        new ActivityScenarioRule<>(
            new Intent(context, WorkerDashboard.class)
                .addCategory(context.getString(R.string.MOCK_DATABASE))
        );
    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    private Database database;

    @Before
    public void setup() {
        ActivityScenario<WorkerDashboard> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Do not run the test if we are not using the mock database.
            assertTrue("Not using Mock Database", activity.getDatabase() instanceof MockDatabase);
            database = activity.getDatabase();
        });

        generateAvailableJobs(database, Assert::fail);
    }

    @Test
    public void searchResultsTest() {
        onView(withId(R.id.workerSearchPage)).perform(click());
        onView(withClassName(is("android.widget.SearchView$SearchAutoComplete")))
            .perform(click(), replaceText("lawn"), closeSoftKeyboard());
        onView(withId(R.id.title)).check(matches(withText("Lawn Mowing")));
    }
}
