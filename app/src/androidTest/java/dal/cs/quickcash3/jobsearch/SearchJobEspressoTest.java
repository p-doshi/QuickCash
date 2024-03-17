package dal.cs.quickcash3.jobsearch;

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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import static dal.cs.quickcash3.jobSearch.ExampleJobList.GOOGLEPLEX;
import static dal.cs.quickcash3.jobSearch.ExampleJobList.JOBS;
import static dal.cs.quickcash3.test.RecyclerViewItemCountMatcher.hasItemCount;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseDirectory;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.location.MockLocationProvider;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class SearchJobEspressoTest {

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
//        locationProvider.setLocation(GOOGLEPLEX);
//        generateJobPosts();
        onView(withId(R.id.workerSearchPage)).perform(click());
        onView(allOf(withClassName(is("android.widget.ImageView")), withContentDescription("Search")))
                .perform(click());
        onView(allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete"))))
                .perform(replaceText("lawn"), pressImeActionButton());
        String expectedJobTitles=
                "Lawn Moving";
        onView(withId(R.id.title)).check(matches(withText(expectedJobTitles)));

//        checkJobPosts(expectedJobTitles);
    }

}
