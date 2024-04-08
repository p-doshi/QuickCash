package dal.cs.quickcash3.jobs;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.generateAvailableJobs;
import static dal.cs.quickcash3.test.ExampleUserList.WORKER1;
import static dal.cs.quickcash3.test.ExampleUserList.generateUsers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class ApplyButtonUITest {
    private static final int MAX_TIMEOUT = 15000;
    private Database database;
    private static final String GROCERIES = "Groceries";
    private static final String SUCCESS = "Application Successful";
    private static final String APPLIED = "You have already applied to this job";
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
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());
    private final String appPackage = ApplicationProvider.getApplicationContext().getPackageName();

    private @NonNull UiObject findResource(@NonNull String resourceId) {
        UiSelector selector = new UiSelector().resourceId(appPackage + ":id/" + resourceId);
        return device.findObject(selector);
    }


    @Before
    public void setup() throws Throwable {
        ActivityScenario<WorkerDashboard> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Do not run the test if we are not using the mock implementation.
            assertTrue("Not using Mock Database", activity.getDatabase() instanceof MockDatabase);

            database = activity.getDatabase();
        });

        runOnUiThread(() -> generateAvailableJobs(database, Assert::fail));
        generateUsers(database, Assert::fail);
        UiObject searchPage = findResource("workerSearchPage");
        assertTrue(searchPage.waitForExists(MAX_TIMEOUT));
        searchPage.click();
        UiObject jobDetailsPage = device.findObject(new UiSelector().textContains(GROCERIES));
        jobDetailsPage.click();
    }
    @Test
    public void checkIfJobDetailsPageIsVisible() {
        assertTrue(findResource("jobTitle").waitForExists(MAX_TIMEOUT));
        assertTrue(findResource("jobAddress").waitForExists(MAX_TIMEOUT));
    }

    @Test
    public void checkIfApplySuccessful() throws UiObjectNotFoundException {
        assertTrue(findResource("jobAddress").waitForExists(MAX_TIMEOUT));
        UiObject applyButton = findResource("applyForJob");
        applyButton.click();
        assertTrue(findResource("statusApplyJob").getText().matches(SUCCESS));
    }

    @Test
    public void checkIfAlreadyApplied() throws UiObjectNotFoundException {
        assertTrue(findResource("jobAddress").waitForExists(MAX_TIMEOUT));
        UiObject applyButton = findResource("applyForJob");
        applyButton.click();
        assertTrue(findResource("statusApplyJob").getText().matches(SUCCESS));
        UiObject searchPage = findResource("workerSearchPage");
        searchPage.click();
        UiObject jobDetailsPage = device.findObject(new UiSelector().textContains(GROCERIES));
        jobDetailsPage.click();
        assertTrue(findResource("statusApplyJob").getText().matches(APPLIED));
    }
}
