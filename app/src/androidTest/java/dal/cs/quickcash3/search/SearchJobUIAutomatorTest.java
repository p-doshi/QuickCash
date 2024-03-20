package dal.cs.quickcash3.search;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.Manifest;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.worker.WorkerDashboard;

public class SearchJobUIAutomatorTest {
    private static final int MAX_TIMEOUT = 5000;
    @Rule
    public final ActivityScenarioRule<WorkerDashboard> activityRule =
            new ActivityScenarioRule<>(WorkerDashboard.class);
    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());

    @After
    public void teardown() {
        // Just to be sure.
        FirebaseAuth.getInstance().signOut();
    }


    @Test
    public void checkIfLandingPageIsVisible() throws UiObjectNotFoundException {
        UiObject searchPage = device.findObject(new UiSelector().resourceId("dal.cs.quickcash3:id/workerSearchPage"));
        assertTrue(searchPage.waitForExists(MAX_TIMEOUT));
        searchPage.click();
        UiObject searchBox = device.findObject(new UiSelector().resourceId("dal.cs.quickcash3:id/searchBar"));
        assertTrue(searchBox.waitForExists(MAX_TIMEOUT));
        UiObject filterIcon = device.findObject(new UiSelector().resourceId("dal.cs.quickcash3:id/filterIcon"));
        assertTrue(filterIcon.waitForExists(MAX_TIMEOUT));
    }


    @Test
    public void checkIfMovedToSearchFilter() throws UiObjectNotFoundException {
        UiObject searchPage = device.findObject(new UiSelector().resourceId("dal.cs.quickcash3:id/workerSearchPage"));
        assertTrue(searchPage.waitForExists(MAX_TIMEOUT));
        searchPage.click();
        UiObject filterIcon = device.findObject(new UiSelector().resourceId("dal.cs.quickcash3:id/filterIcon"));
        assertTrue(filterIcon.waitForExists(MAX_TIMEOUT));
        filterIcon.click();
        UiObject welcomeLabel = device.findObject(new UiSelector().resourceId("dal.cs.quickcash3:id/filterFragment"));
        assertTrue(welcomeLabel.waitForExists(MAX_TIMEOUT));
    }


    @Test
    public void checkIfMovedToJobSearchPage() throws UiObjectNotFoundException {
        UiObject searchPage = device.findObject(new UiSelector().resourceId("dal.cs.quickcash3:id/workerSearchPage"));
        assertTrue(searchPage.waitForExists(MAX_TIMEOUT));
        searchPage.click();
        UiObject filterIcon = device.findObject(new UiSelector().resourceId("dal.cs.quickcash3:id/filterIcon"));
        assertTrue(filterIcon.waitForExists(MAX_TIMEOUT));
        filterIcon.click();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains("Job Search"));
        assertTrue(welcomeLabel.waitForExists(MAX_TIMEOUT));
        UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
        scrollable.setAsVerticalList();
        scrollable.scrollForward();
        UiObject searchButton = device.findObject(new UiSelector().resourceId("dal.cs.quickcash3:id/searchButton"));
        assertTrue(searchButton.waitForExists(MAX_TIMEOUT));
        searchButton.click();
        UiObject searchBox = device.findObject(new UiSelector().resourceId("dal.cs.quickcash3:id/searchBar"));
        assertTrue(searchBox.waitForExists(MAX_TIMEOUT));
    }
}
