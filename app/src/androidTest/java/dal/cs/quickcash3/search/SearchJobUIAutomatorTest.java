package dal.cs.quickcash3.search;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
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
    private final String appPackage = ApplicationProvider.getApplicationContext().getPackageName();

    private @NonNull UiObject findResource(@NonNull String resourceId) {
        UiSelector selector = new UiSelector().resourceId(appPackage + ":id/" + resourceId);
        return device.findObject(selector);
    }

    @Before
    public void setup() throws UiObjectNotFoundException {
        UiObject searchPage = findResource("workerSearchPage");
        assertTrue(searchPage.waitForExists(MAX_TIMEOUT));
        searchPage.click();
    }

    @Test
    public void checkIfLandingPageIsVisible() {
        assertTrue(findResource("searchBar").waitForExists(MAX_TIMEOUT));
        assertTrue(findResource("filterIcon").waitForExists(MAX_TIMEOUT));
    }

    @Test
    public void checkIfMovedToSearchFilter() throws UiObjectNotFoundException {
        UiObject filterIcon = findResource("filterIcon");
        assertTrue(filterIcon.waitForExists(MAX_TIMEOUT));
        filterIcon.click();
        assertTrue(findResource("filterFragment").waitForExists(MAX_TIMEOUT));
    }

    @Test
    public void checkIfMovedToJobSearchPage() throws UiObjectNotFoundException {
        UiObject filterIcon = findResource("filterIcon");
        assertTrue(filterIcon.waitForExists(MAX_TIMEOUT));
        filterIcon.click();
        UiObject applyButton = findResource("applyButton");
        assertTrue(applyButton.waitForExists(MAX_TIMEOUT));
        applyButton.click();
        assertTrue(findResource("searchBar").waitForExists(MAX_TIMEOUT));
    }
}
