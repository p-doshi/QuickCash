package dal.cs.quickcash3.login;

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

public class LoginUIAutomatorTest {
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String CONTINUE = "Continue";
    private static final String WELCOME = "Welcome";
    private static final String PARTH_GMAIL = "parthdoshi135@gmail.com";
    @Rule
    public final ActivityScenarioRule<LoginActivity> activityRule =
        new ActivityScenarioRule<>(LoginActivity.class);
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());

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
        UiObject roleSpinner = device.findObject(new UiSelector().textContains(PASSWORD));
        assertTrue(roleSpinner.exists());
        UiObject registerButton = device.findObject(new UiSelector().text(CONTINUE));
        assertTrue(registerButton.exists());
    }

    //below tests wont work until pages connected :(
    @Test
    public void checkIfMovedToEmployerDashboard() throws UiObjectNotFoundException {

        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        emailIDBox.setText(PARTH_GMAIL);
        UiObject passwordBox = device.findObject(new UiSelector().textContains(PASSWORD));
        passwordBox.setText(PASSWORD);
        UiObject registerButton = device.findObject(new UiSelector().text(CONTINUE));
        registerButton.clickAndWaitForNewWindow();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains("Current"));
        assertTrue(welcomeLabel.exists());
    }

    @Test
    public void checkIfMovedToEmployeeDashboard() throws UiObjectNotFoundException {

        UiObject emailIDBox = device.findObject(new UiSelector().textContains("Email"));
        emailIDBox.setText("ethroz@gmail.com");
        UiObject passwordBox = device.findObject(new UiSelector().textContains("Password"));
        passwordBox.setText("Password");
        UiObject registerButton = device.findObject(new UiSelector().text("Continue"));
        registerButton.clickAndWaitForNewWindow();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains("Jobs"));
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
    public void checkIfMovedToSignUpPage() throws UiObjectNotFoundException {
        UiObject signUpButton = device.findObject(new UiSelector().text("Sign Up Manually"));
        signUpButton.clickAndWaitForNewWindow();
        UiObject confirmPasswordLabel = device.findObject(new UiSelector().textContains("Confirm Password"));
        assertTrue(confirmPasswordLabel.exists());
    }

}
