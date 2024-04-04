package dal.cs.quickcash3.worker;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.app.Instrumentation;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.registration.RegistrationPage;

public class PreferancesUITest {
    @Rule
    public final ActivityScenarioRule<StorePreferencesActivity> activityRule =
            new ActivityScenarioRule<>(StorePreferencesActivity.class);
    private final Instrumentation instrumentation = getInstrumentation();
    private final Context context = instrumentation.getTargetContext();
    private final UiDevice device = UiDevice.getInstance(instrumentation);
    private final String appPackage = context.getPackageName();

    private @NonNull UiObject findText(@NonNull String text) {
        UiSelector selector = new UiSelector().text(text);
        return device.findObject(selector);
    }

    private @NonNull UiObject findResource(@NonNull String resourceId) {
        UiSelector selector = new UiSelector().resourceId(appPackage + ":id/" + resourceId);
        return device.findObject(selector);
    }

    @Test
    public void checkIfUIExists() throws UiObjectNotFoundException {
        assertTrue(findText("Salary Range").exists());
        assertTrue(findResource("preferenceSalaryRangeSlider").exists());
        assertTrue(findText("Duration Range").exists());
        assertTrue(findResource("preferenceDurationRangeSlider").exists());
        assertTrue(findText("Disable").exists());
        assertTrue(findResource("durationCheckBox").isCheckable());
        assertTrue(findResource("salaryCheckBox").isCheckable());
        assertTrue(findText("Update").isClickable());
    }
}
