package dal.cs.quickcash3.fragments;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.JobPostHelper;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.location.MockLocationProvider;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class SearchUITests {
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

    @Test
    public void checkIfUIExists() throws UiObjectNotFoundException {
        UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
        scrollable.scrollIntoView(new UiSelector().text("Job Search"));
        scrollable.scrollIntoView(new UiSelector().text("Job Title"));
        scrollable.scrollIntoView(new UiSelector().text("Pay Range"));
        scrollable.scrollIntoView(new UiSelector().resourceId(appPackage + ":id/payRangeSlider"));
        scrollable.scrollIntoView(new UiSelector().text("Time Estimate"));
        scrollable.scrollIntoView(new UiSelector().resourceId(appPackage + ":id/timeEstimateSlider"));
        scrollable.scrollIntoView(new UiSelector().text("Max Distance"));
        scrollable.scrollIntoView(new UiSelector().resourceId(appPackage + ":id/maxDistanceSlider"));
        scrollable.scrollIntoView(new UiSelector().text("Search").clickable(true));
    }

    @Ignore("Missing connections")
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops") // No.
    @Test
    public void successfulSearch() throws UiObjectNotFoundException {
        LatLng southwest = new LatLng(0.0,0.0);
        LatLng northeast = new LatLng(0.0001,0.0001); // Roughly 11 m side length
        LatLngBounds locationBounds = new LatLngBounds(southwest, northeast);
        List<AvailableJob> jobs = JobPostHelper.generateAvailable(5, locationBounds);
        for (AvailableJob job : jobs) {
            job.writeToDatabase(database, Assert::fail);
        }

        locationProvider.setLocation(locationBounds.getCenter());

        UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
        UiSelector buttonSelector = new UiSelector().text("Search").clickable(true);
        scrollable.scrollIntoView(buttonSelector);
        UiObject searchButton = device.findObject(buttonSelector);
        searchButton.clickAndWaitForNewWindow();

        scrollable = new UiScrollable(new UiSelector().scrollable(true));
        for (AvailableJob job : jobs) {
            scrollable.scrollIntoView(new UiSelector().text(job.getTitle()));
            scrollable.scrollIntoView(new UiSelector().textContains(String.valueOf(job.getSalary())));
            scrollable.scrollIntoView(new UiSelector().text(String.valueOf(job.getDuration())));
        }
    }

    @Ignore("Missing connections")
    @Test
    public void failedSearch() throws UiObjectNotFoundException {
        LatLng southwest = new LatLng(0.0,0.0);
        LatLng northeast = new LatLng(0.0001,0.0001); // Roughly 11 m side length
        LatLngBounds locationBounds = new LatLngBounds(southwest, northeast);
        List<AvailableJob> jobs = JobPostHelper.generateAvailable(4, locationBounds);
        for (AvailableJob job : jobs) {
            job.writeToDatabase(database, Assert::fail);
        }

        locationProvider.setLocation(new LatLng(10.0,10.0)); // Location too far from job postings.

        UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
        UiSelector buttonSelector = new UiSelector().text("Search").clickable(true);
        scrollable.scrollIntoView(buttonSelector);
        UiObject searchButton = device.findObject(buttonSelector);
        searchButton.clickAndWaitForNewWindow();

        assertTrue(device.findObject(new UiSelector().textContains("There are no results")).exists());
    }
}
