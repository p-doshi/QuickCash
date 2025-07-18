package dal.cs.quickcash3.search;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.GOOGLEPLEX;
import static dal.cs.quickcash3.test.ExampleJobList.AVAILABLE_JOBS;
import static dal.cs.quickcash3.test.ExampleJobList.NEW_YORK;
import static dal.cs.quickcash3.test.ExampleJobList.generateAvailableJobs;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.location.MockLocationProvider;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class SearchFiltersUITests {
    private static final int DESCRIPTION_SIZE = 20;
    private final Instrumentation instrumentation = getInstrumentation();
    private final Context context = instrumentation.getTargetContext();
    @Rule
    public final ActivityScenarioRule<WorkerDashboard> activityRule =
        new ActivityScenarioRule<>(
            new Intent(context, WorkerDashboard.class)
                .addCategory(context.getString(R.string.MOCK_DATABASE))
                .addCategory(context.getString(R.string.MOCK_LOCATION))
        );
    private final UiDevice device = UiDevice.getInstance(instrumentation);
    private final String appPackage = context.getPackageName();
    private Database database;
    private MockLocationProvider locationProvider;

    private @NonNull UiObject findText(@NonNull UiScrollable scrollable, @NonNull String text) throws UiObjectNotFoundException {
        UiSelector selector = new UiSelector().text(text);
        scrollable.scrollIntoView(selector);
        return device.findObject(selector);
    }

    private @NonNull UiObject findSubstring(@NonNull UiScrollable scrollable, @NonNull String substring) throws UiObjectNotFoundException {
        UiSelector selector = new UiSelector().textContains(substring);
        scrollable.scrollIntoView(selector);
        return device.findObject(selector);
    }

    private @NonNull UiObject findText(@NonNull String text) {
        UiSelector selector = new UiSelector().text(text);
        return device.findObject(selector);
    }

    private @NonNull UiObject findSubstring(@NonNull String substring) {
        UiSelector selector = new UiSelector().textContains(substring);
        return device.findObject(selector);
    }

    private @NonNull UiObject findResource(@NonNull String resourceId) {
        UiSelector selector = new UiSelector().resourceId(appPackage + ":id/" + resourceId);
        return device.findObject(selector);
    }

    @Before
    public void setup() throws UiObjectNotFoundException {
        ActivityScenario<WorkerDashboard> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Do not run the test if we are not using the mock implementations.
            assertTrue("Not using Mock Database", activity.getDatabase() instanceof MockDatabase);
            assertTrue("Not using Mock Location Provider", activity.getLocationProvider() instanceof MockLocationProvider);

            database = activity.getDatabase();
            locationProvider = (MockLocationProvider)activity.getLocationProvider();
        });

        // Navigate to the search filter.
        findResource("workerSearchPage").click();
        findResource("filterIcon").click();
    }

    @Test
    public void checkIfUIExists() throws UiObjectNotFoundException {
        assertTrue(findText("Salary Range").exists());
        assertTrue(findResource("salaryRangeSlider").exists());
        assertTrue(findText("Duration Range").exists());
        assertTrue(findResource("durationRangeSlider").exists());
        assertTrue(findText("Max Distance").exists());
        assertTrue(findResource("maxDistanceSlider").exists());
        assertTrue(findText("Apply Filters").isClickable());
    }

    @Test
    public void successfulSearch() throws Throwable {
        locationProvider.setLocation(GOOGLEPLEX);
        runOnUiThread(() -> generateAvailableJobs(database, Assert::fail));

        findText("Apply Filters").click();

        List<String> excludedJobTitles = Collections.singletonList(
            "Snow Removal"
        );

        UiScrollable resultsPage = new UiScrollable(new UiSelector().resourceId(appPackage + ":id/jobListRecyclerView"));
        for (AvailableJob job : AVAILABLE_JOBS) {
            if (!excludedJobTitles.contains(job.getTitle())) {
                assertNotNull(job.getTitle());
                assertNotNull(job.getDescription());
                assertTrue(findText(resultsPage, job.getTitle()).exists());
                assertTrue(findSubstring(resultsPage, job.getDescription().substring(0, DESCRIPTION_SIZE)).exists());
            }
        }
    }

    @Test
    public void failedSearch() throws UiObjectNotFoundException {
        locationProvider.setLocation(NEW_YORK);
        generateAvailableJobs(database, Assert::fail);

        findText("Apply Filters").click();

        for (AvailableJob job : AVAILABLE_JOBS) {
            assertNotNull(job.getTitle());
            assertNotNull(job.getDescription());
            assertFalse(findText(job.getTitle()).exists());
            assertFalse(findSubstring(job.getDescription().substring(0, DESCRIPTION_SIZE)).exists());
        }
    }
}
