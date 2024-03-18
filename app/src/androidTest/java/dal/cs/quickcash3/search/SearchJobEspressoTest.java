package dal.cs.quickcash3.search;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import static dal.cs.quickcash3.test.WaitForAction.waitFor;

import android.Manifest;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class SearchJobEspressoTest {
    private static final int MAX_DATABASE_TIMEOUT = 5000;
    @Rule
    public final ActivityScenarioRule<WorkerDashboard> activityRule =
            new ActivityScenarioRule<>(WorkerDashboard.class);
    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
//    private Database database;
//    private MockLocationProvider locationProvider;
//
//    private void generateJobPosts() {
//        for (Map.Entry<String, AvailableJob> entry : JOBS.entrySet()) {
//            database.write(
//                    DatabaseDirectory.AVAILABLE_JOBS.getValue() + entry.getKey(),
//                    entry.getValue(),
//                    Assert::fail);
//        }
//    }
//    private void checkJobPosts(@NonNull List<String> expectedJobTitles) {
//        onView(withId(R.id.jobListRecyclerView)).check(matches(hasItemCount(expectedJobTitles.size())));
//
//        for (AvailableJob job : JOBS.values()) {
//            if (expectedJobTitles.contains(job.getTitle())) {
//                onView(allOf(withId(R.id.title), withText(job.getTitle())));
//                onView(allOf(withId(R.id.subhead), withText(job.getDescription())));
//            }
//        }
//    }
//    @Before
//    public void setup() {
//        ActivityScenario<WorkerDashboard> scenario = activityRule.getScenario();
//        scenario.onActivity(activity -> {
//            // Do not run the test if we are not using the mock implementation.
//            assertTrue("Not using Mock Database", activity.getDatabase() instanceof MockDatabase);
//            assertTrue("Not using Mock Location Provider", activity.getLocationProvider() instanceof MockLocationProvider);
//
//            database = activity.getDatabase();
//            locationProvider = (MockLocationProvider)activity.getLocationProvider();
//        });
//
//        // Navigate to the search filter.
//        onView(withId(R.id.workerSearchPage)).perform(click());
//        onView(withId(R.id.filterIcon)).perform(click());
//    }

    @Test
    public void searchResultsTest() {
        onView(withId(R.id.workerSearchPage)).perform(click());
        onView(allOf(withClassName(is("android.widget.ImageView")), withContentDescription("Search")))
                .perform(click());
        onView(withClassName(is("android.widget.SearchView$SearchAutoComplete")))
                .perform(replaceText("lawn"), pressImeActionButton());
        Espresso.onIdle();
        onView(withId(R.id.title)).check(matches(withText("Lawn Moving")));
    }
}
