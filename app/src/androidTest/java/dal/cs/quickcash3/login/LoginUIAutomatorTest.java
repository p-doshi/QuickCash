package dal.cs.quickcash3.login;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class LoginUIAutomatorTest {
    private static final int LAUNCH_TIMEOUT = 10000;
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String CONTINUE = "Continue";
    private static final String WELCOME = "Welcome";
    private static final String PARTH_GMAIL = "parthdoshi135@gmail.com";
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());
    public Context context;
    @Rule
    public final ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);
    @Before
    public void setup() throws IOException {
        device.executeShellCommand("settings put secure show_ime_with_hard_keyboard 0");
        this.context = ApplicationProvider.getApplicationContext();
        launchApp();
    }

    public void launchApp() {
        final Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("dal.cs.quickcash3");
        assert appIntent != null;
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(appIntent);
        device.wait(Until.hasObject(By.pkg("dal.cs.quickcash3").depth(0)), LAUNCH_TIMEOUT);
    }
    @After
    public void teardown() {
        // Just to be sure.
        FirebaseAuth.getInstance().signOut();
    }

    private void relaunchApp() {
        ActivityScenario<LoginActivity> scenario = activityRule.getScenario();
        scenario.recreate();
    }

    @Test
    public void checkIfLandingPageIsVisible() {
        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        assertTrue(emailIDBox.exists());
        UiObject passwordBox= device.findObject(new UiSelector().textContains(PASSWORD));
        assertTrue(passwordBox.exists());
        UiObject registerButton = device.findObject(new UiSelector().text(CONTINUE));
        assertTrue(registerButton.exists());
    }


    //below tests wont work until pages connected :(
    @Test
    @Ignore("only after pages connected")
    public void checkIfMovedToDashboard() throws UiObjectNotFoundException {

        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        emailIDBox.setText(PARTH_GMAIL);
        UiObject passwordBox = device.findObject(new UiSelector().textContains(PASSWORD));
        passwordBox.setText(PASSWORD);
        UiObject registerButton = device.findObject(new UiSelector().text(CONTINUE));
        registerButton.clickAndWaitForNewWindow();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains(WELCOME));
        assertTrue(welcomeLabel.exists());
    }

    @Test
    @Ignore("only after pages connected")
    public void checkIfRememberMeWorks() throws UiObjectNotFoundException {
        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        emailIDBox.setText(PARTH_GMAIL);
        UiObject passwordBox = device.findObject(new UiSelector().textContains(PASSWORD));
        passwordBox.setText(PASSWORD);
        UiObject checkbox = device.findObject(new UiSelector().textContains("Remember"));
        checkbox.click();
        UiObject registerButton = device.findObject(new UiSelector().text(CONTINUE));
        registerButton.click();
        relaunchApp();
    }

    @Test
    @Ignore("only after pages connected")
    public void checkIfMovedToSignUpPage() throws UiObjectNotFoundException {

        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        emailIDBox.setText(PARTH_GMAIL);
        UiObject passwordBox = device.findObject(new UiSelector().textContains(PASSWORD));
        passwordBox.setText(PASSWORD);
        UiObject registerButton = device.findObject(new UiSelector().text(CONTINUE));
        registerButton.clickAndWaitForNewWindow();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains(WELCOME));
        assertTrue(welcomeLabel.exists());
    }
}
