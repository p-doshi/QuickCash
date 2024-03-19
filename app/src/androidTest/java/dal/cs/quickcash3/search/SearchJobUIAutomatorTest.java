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

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.worker.WorkerDashboard;

public class SearchJobUIAutomatorTest {
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
        findResource("workerSearchPage").click();
    }

    @Test
    public void checkIfLandingPageIsVisible() {
        assertTrue(findResource("searchBar").exists());
        assertTrue(findResource("filterIcon").exists());
    }

    @Test
    public void checkIfMovedToSearchFilter() throws UiObjectNotFoundException {
        findResource("filterIcon").click();
        assertTrue(findResource("filterFragment").exists());
    }

    @Test
    public void checkIfMovedToJobSearchPage() throws UiObjectNotFoundException {
        findResource("filterIcon").click();
        findResource("applyButton").click();
        assertTrue(findResource("searchBar").exists());
    }
}
