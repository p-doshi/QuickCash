package com.example.csci3130_group_3;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;

public class UIAutomatorTest {
    private static final int LAUNCH_TIMEOUT = 5000;
    public View decorView;

    final String launcherPackage = "com.example.csci3130_group_3";
    private UiDevice device;
    @Before
    public void setup() {
        // Get an instance of UiDevice for interacting with the device
        device = UiDevice.getInstance(getInstrumentation());
        // Get the application context
        Context context = ApplicationProvider.getApplicationContext();
        // Retrieve the launch intent for the specified package
        final Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackage);
        // Add the flag to clear the task before launching the application
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Start the application using the launch intent
        context.startActivity(appIntent);
        // Wait for the launcher package to be in the foreground
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);


    }

    @Test
    public void testValidCredentials() {
        onView(withId(R.id.emailaddress)).perform(typeText("pdoshi@gmail.com"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etPassword)).perform(typeText("hi"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.continueButton)).perform(click());
        onView(withText(R.string.VALID_TOAST))
                .inRoot(withDecorView(not(is(decorView))))
                .check(matches(isDisplayed()));
    }


}
