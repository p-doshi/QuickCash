package dal.cs.quickcash3.search;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.DALHOUSIE;
import static dal.cs.quickcash3.test.ExampleJobList.GOOGLEPLEX;
import static dal.cs.quickcash3.test.ExampleJobList.JOBS;
import static dal.cs.quickcash3.test.ExampleJobList.generateJobPosts;
import static dal.cs.quickcash3.test.RangeSliderSwiper.adjustRangeSliderThumbs;
import static dal.cs.quickcash3.test.RecyclerViewItemCountMatcher.recyclerHasItemCount;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.location.MockLocationProvider;
import dal.cs.quickcash3.worker.WorkerDashboard;

@SuppressWarnings("PMD.ExcessiveImports") // No.
public class SearchFiltersEspressoTests {
    private final Instrumentation instrumentation = getInstrumentation();
    private final Context context = instrumentation.getTargetContext();
    @Rule
    public final ActivityScenarioRule<WorkerDashboard> activityRule =
        new ActivityScenarioRule<>(
            new Intent(context, WorkerDashboard.class)
                .addCategory(context.getString(R.string.MOCK_DATABASE))
                .addCategory(context.getString(R.string.MOCK_LOCATION))
        );
    private Database database;
    private MockLocationProvider locationProvider;

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
        ActivityScenario<WorkerDashboard> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Do not run the test if we are not using the mock implementation.
            assertTrue("Not using Mock Database", activity.getDatabase() instanceof MockDatabase);
            assertTrue("Not using Mock Location Provider", activity.getLocationProvider() instanceof MockLocationProvider);

            database = activity.getDatabase();
            locationProvider = (MockLocationProvider)activity.getLocationProvider();
        });

        // Navigate to the search filter.
        onView(withId(R.id.workerSearchPage)).perform(click());
        onView(withId(R.id.filterIcon)).perform(click());
    }

    @Ignore("Missing implementation")
    @Test
    public void hundredMeterSearch() {
        locationProvider.setLocation(GOOGLEPLEX);

        generateJobPosts(database, Assert::fail);

        onView(withId(R.id.maxDistanceSlider)).perform(adjustRangeSliderThumbs(0.0f));
        onView(withId(R.id.searchButton)).perform(click());

        List<String> expectedJobTitles = Collections.singletonList(
            "Coding problem"
        );

        checkJobPosts(expectedJobTitles);
    }

    @Ignore("Missing implementation")
    @Test
    public void fiveKmSearch() {
        locationProvider.setLocation(GOOGLEPLEX);

        generateJobPosts(database, Assert::fail);

        onView(withId(R.id.maxDistanceSlider)).perform(adjustRangeSliderThumbs(0.5f));
        onView(withId(R.id.searchButton)).perform(click());

        List<String> expectedJobTitles = Arrays.asList(
            "Walk Dog",
            "Groceries",
            "Coding problem",
            "Landscaping"
        );

        checkJobPosts(expectedJobTitles);
    }

    @Ignore("Missing implementation")
    @Test
    public void differentLocation() {
        locationProvider.setLocation(DALHOUSIE);

        generateJobPosts(database, Assert::fail);

        onView(withId(R.id.searchButton)).perform(click());

        List<String> expectedJobTitles = Collections.singletonList(
            "Snow Removal"
        );

        checkJobPosts(expectedJobTitles);
    }

    @Test
    public void greaterThanOneDay() {
        locationProvider.setLocation(GOOGLEPLEX);

        generateJobPosts(database, Assert::fail);

        onView(withId(R.id.durationRangeSlider)).perform(adjustRangeSliderThumbs(0.5f, 1.0f));
        onView(withId(R.id.searchButton)).perform(click());

        List<String> expectedJobTitles = Collections.singletonList(
            "Landscaping"
        );

        checkJobPosts(expectedJobTitles);
    }

    @Test
    public void lessThan40Dollars() {
        locationProvider.setLocation(GOOGLEPLEX);

        generateJobPosts(database, Assert::fail);

        onView(withId(R.id.salaryRangeSlider)).perform(adjustRangeSliderThumbs(0.0f, 0.4f));
        onView(withId(R.id.searchButton)).perform(click());

        List<String> expectedJobTitles = Arrays.asList(
            "Walk Dog",
            "Groceries",
            "Coding problem"
        );

        checkJobPosts(expectedJobTitles);
    }
}
