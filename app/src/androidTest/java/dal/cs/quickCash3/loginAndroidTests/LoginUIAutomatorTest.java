package dal.cs.quickCash3.loginAndroidTests;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import dal.cs.quickCash3.login.LoginActivity;

public class LoginUIAutomatorTest {
    @Rule
    public final ActivityScenarioRule<LoginActivity> activityRule =
        new ActivityScenarioRule<>(LoginActivity.class);
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String CONTINUE = "Continue";
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());

    private void relaunchActivity() {
        ActivityScenario<LoginActivity> scenario = activityRule.getScenario();
        scenario.recreate();
    }

    @After
    public void teardown() {
        // Just to be sure.
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void checkIfLandingPageIsVisible() {
        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        assertTrue(emailIDBox.exists());
        UiObject roleSpinner = device.findObject(new UiSelector().textContains(PASSWORD));
        assertTrue(roleSpinner.exists());
        UiObject registerButton = device.findObject(new UiSelector().text(CONTINUE));
        assertTrue(registerButton.exists());
    }


    //below tests wont work until pages connected :(
    @Test
    @Ignore("only after pages connected")
    public void checkIfMovedToDashboard() throws UiObjectNotFoundException {
        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        emailIDBox.setText("parthdoshi135@gmail.com");
        UiObject passwordBox = device.findObject(new UiSelector().textContains(PASSWORD));
        passwordBox.setText(PASSWORD);
        UiObject registerButton = device.findObject(new UiSelector().text(CONTINUE));
        registerButton.clickAndWaitForNewWindow();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains("Welcome"));
        assertTrue(welcomeLabel.exists());
    }

    @Test
    @Ignore("only after pages connected")
    public void checkIfRememberMeWorks() throws UiObjectNotFoundException, IOException {
        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        emailIDBox.setText("parthdoshi135@gmail.com");
        UiObject passwordBox = device.findObject(new UiSelector().textContains(PASSWORD));
        passwordBox.setText(PASSWORD);
        UiObject checkbox = device.findObject(new UiSelector().textContains("Remember"));
        checkbox.click();
        UiObject registerButton = device.findObject(new UiSelector().text(CONTINUE));
        registerButton.click();
        relaunchActivity();
    }

    @Test
    @Ignore("only after pages connected")
    public void checkIfMovedToSignUpPage() throws UiObjectNotFoundException {
        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        emailIDBox.setText("parthdoshi135@gmail.com");
        UiObject passwordBox = device.findObject(new UiSelector().textContains(PASSWORD));
        passwordBox.setText(PASSWORD);
        UiObject registerButton = device.findObject(new UiSelector().text(CONTINUE));
        registerButton.clickAndWaitForNewWindow();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains("Welcome"));
        assertTrue(welcomeLabel.exists());
    }
}
