package dal.cs.quickcash3.login;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;


import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.employer.EmployerDashboard;
import dal.cs.quickcash3.registration.RegistrationPage;
import dal.cs.quickcash3.test.ActivityMonitorRule;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class LoginUIAutomatorTest {
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String CONTINUE = "Continue";
    private static final String PARTH_GMAIL = "parthdoshi135@gmail.com";
    private static final String ETHAN_GMAIL = "ethroz@gmail.com";
    private static final String REMEMBER = "remember";
    private static final int MAX_TIMEOUT = 30000;

    @Rule
    public final ActivityMonitorRule<EmployerDashboard> employerDashboard =
        new ActivityMonitorRule<>(EmployerDashboard.class);
    @Rule
    public final ActivityMonitorRule<WorkerDashboard> workerDashboard =
        new ActivityMonitorRule<>(WorkerDashboard.class);
    @Rule
    public final ActivityMonitorRule<RegistrationPage> registrationPage =
        new ActivityMonitorRule<>(RegistrationPage.class);
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());

    private void launchApp() {
        ActivityScenario.launch(LoginActivity.class);
    }

    @Before
    public void setup() {
        FirebaseAuth.getInstance().signOut();
        launchApp();
    }

    @After
    public void teardown() {
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

    @Test
    public void checkIfMovedToEmployerDashboard() throws UiObjectNotFoundException {
        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        emailIDBox.setText(PARTH_GMAIL);
        UiObject passwordBox = device.findObject(new UiSelector().textContains(PASSWORD));
        passwordBox.setText(PASSWORD);
        UiObject signInButton = device.findObject(new UiSelector().text(CONTINUE));
        signInButton.click();
        employerDashboard.waitForActivity(MAX_TIMEOUT).finish();
    }

    @Test
    public void checkIfMovedToWorkerDashboard() throws UiObjectNotFoundException {
        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        emailIDBox.setText(ETHAN_GMAIL);
        UiObject passwordBox = device.findObject(new UiSelector().textContains(PASSWORD));
        passwordBox.setText(PASSWORD);
        UiObject signInButton = device.findObject(new UiSelector().text(CONTINUE));
        signInButton.click();
        workerDashboard.waitForActivity(MAX_TIMEOUT).finish();
    }

    @Test
    public void checkIfRememberMeWorks() throws UiObjectNotFoundException {
        UiObject emailIDBox = device.findObject(new UiSelector().textContains(EMAIL));
        emailIDBox.setText(PARTH_GMAIL);
        UiObject passwordBox = device.findObject(new UiSelector().textContains(PASSWORD));
        passwordBox.setText(PASSWORD);
        UiObject checkbox = device.findObject(new UiSelector().textContains(REMEMBER));
        checkbox.click();
        UiObject signInButton = device.findObject(new UiSelector().text(CONTINUE));
        signInButton.click();
        employerDashboard.waitForActivity(MAX_TIMEOUT).finish();
        launchApp();
        employerDashboard.waitForActivity(MAX_TIMEOUT).finish();
    }

    @Test
    public void checkIfMovedToSignUpPage() throws UiObjectNotFoundException {
        UiObject signUpButton = device.findObject(new UiSelector().text("Sign Up Manually"));
        signUpButton.click();
        registrationPage.waitForActivity(MAX_TIMEOUT).finish();
    }
}
