package dal.cs.quickcash3.login;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.Build;
import android.provider.Settings;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.R;

public class LoginEspressoTest {
    public ActivityScenario<LoginActivity> scenario;
    @Rule
    public final ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(LoginActivity::setUpLoginButton);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // On newer versions, we can disable window animations using developer settings
            InstrumentationRegistry.getInstrumentation().getUiAutomation()
                    .executeShellCommand("settings put global window_animation_scale 0");
            InstrumentationRegistry.getInstrumentation().getUiAutomation()
                    .executeShellCommand("settings put global transition_animation_scale 0");
            InstrumentationRegistry.getInstrumentation().getUiAutomation()
                    .executeShellCommand("settings put global animator_duration_scale 0");
        } else {
            // On older versions, we need to change system settings directly
            Settings.Global.putInt(
                    InstrumentationRegistry.getInstrumentation().getContext().getContentResolver(),
                    Settings.Global.WINDOW_ANIMATION_SCALE, 0);
            Settings.Global.putInt(
                    InstrumentationRegistry.getInstrumentation().getContext().getContentResolver(),
                    Settings.Global.TRANSITION_ANIMATION_SCALE, 0);
            Settings.Global.putInt(
                    InstrumentationRegistry.getInstrumentation().getContext().getContentResolver(),
                    Settings.Global.ANIMATOR_DURATION_SCALE, 0);
        }    }

    @After
    public void teardown() {
        // Just to be sure.
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void testEmptyEmail() {
        onView(withId(R.id.emailaddress)).perform(typeText("")).perform(closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("hi")).perform(closeSoftKeyboard());
        onView(withId(R.id.continueButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_EMAIL_TOAST)));
    }

    @Test
    public void testEmptyPassword() {
        onView(withId(R.id.emailaddress)).perform(typeText("pdoshi@gmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("")).perform(closeSoftKeyboard());
        onView(withId(R.id.continueButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_PASSWORD_TOAST)));
        Assert.assertNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Test
    public void testInvalidEmail() {
        onView(withId(R.id.emailaddress)).perform(typeText("pdoshigmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("hahapranked")).perform(closeSoftKeyboard());
        onView(withId(R.id.continueButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.INVALID_EMAIL_TOAST)));
    }

    @Test
    public void testInvalidCredentials() throws InterruptedException {
        onView(withId(R.id.emailaddress)).perform(typeText("parthdoshi135@gmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("hahapranked")).perform(closeSoftKeyboard());
        onView(withId(R.id.continueButton)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.statusLabel)).check(matches(withText("Wrong email or password")));
    }
}
