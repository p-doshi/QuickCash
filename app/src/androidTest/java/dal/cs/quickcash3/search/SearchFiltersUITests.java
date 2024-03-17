package dal.cs.quickcash3.search;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.JobPostHelper;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.location.LocationHelper;
import dal.cs.quickcash3.location.MockLocationProvider;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class SearchFiltersUITests {
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
        UiObject searchMenuOption = device.findObject(new UiSelector().resourceId(appPackage + ":id/workerSearchPage").clickable(true));
        searchMenuOption.click();
        UiObject filterOption = device.findObject(new UiSelector().resourceId(appPackage + ":id/filterIcon").clickable(true));
        filterOption.click();
    }

    private @NonNull UiObject findText(@NonNull UiScrollable scrollable, @NonNull String text) throws UiObjectNotFoundException {
        UiSelector selector = new UiSelector().text(text);
        scrollable.scrollIntoView(selector);
        return device.findObject(selector);
    }

    private @NonNull UiObject findResource(@NonNull UiScrollable scrollable, @NonNull String id) throws UiObjectNotFoundException {
        UiSelector selector = new UiSelector().resourceId(appPackage + ":id/" + id);
        scrollable.scrollIntoView(selector);
        return device.findObject(selector);
    }

    private @NonNull UiObject findText(@NonNull String text) {
        UiSelector selector = new UiSelector().text(text);
        return device.findObject(selector);
    }

    @Test
    public void checkIfUIExists() throws UiObjectNotFoundException {
        UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
        Assert.assertTrue(findText(scrollable, "Salary Range").exists());
        Assert.assertTrue(findResource(scrollable, "salaryRangeSlider").exists());
        Assert.assertTrue(findText(scrollable, "Duration Range").exists());
        Assert.assertTrue(findResource(scrollable, "durationRangeSlider").exists());
        Assert.assertTrue(findText(scrollable, "Max Distance").exists());
        Assert.assertTrue(findResource(scrollable, "maxDistanceSlider").exists());
        Assert.assertTrue(findText(scrollable, "Apply Filters").isClickable());
    }

    private @NonNull List<AvailableJob> generateJobsAround(@NonNull LatLng location, double radius) {
        LatLngBounds locationBounds = LocationHelper.getBoundingBox(location, radius);
        List<AvailableJob> jobs = JobPostHelper.generateAvailable(5, locationBounds);
        for (AvailableJob job : jobs) {
            job.writeToDatabase(database, Assert::fail);
        }
        return jobs;
    }

    @Test
    public void successfulSearch() throws UiObjectNotFoundException {
        LatLng location = new LatLng(0.0, 0.0);
        List<AvailableJob> jobs = generateJobsAround(location, 50.0);
        locationProvider.setLocation(location);

        UiScrollable filterPage = new UiScrollable(new UiSelector().scrollable(true));
        UiObject button = findText(filterPage, "Apply Filters");
        button.clickAndWaitForNewWindow();

        UiScrollable resultsPage = new UiScrollable(new UiSelector().resourceId(appPackage + ":id/jobListRecyclerView"));
        for (AvailableJob job : jobs) {
            Assert.assertTrue(findText(resultsPage, job.getTitle()).exists());
            Assert.assertTrue(findText(resultsPage, job.getDescription()).exists());
        }
    }

    @Test
    public void failedSearch() throws UiObjectNotFoundException {
        List<AvailableJob> jobs = generateJobsAround(new LatLng(0.0, 0.0), 1000.0);
        locationProvider.setLocation(new LatLng(1.0, 1.0));

        UiScrollable filterPage = new UiScrollable(new UiSelector().scrollable(true));
        UiObject button = findText(filterPage, "Apply Filters");
        button.clickAndWaitForNewWindow();

        for (AvailableJob job : jobs) {
            Assert.assertFalse(findText(job.getTitle()).exists());
            Assert.assertFalse(findText(job.getDescription()).exists());
        }
    }
}
