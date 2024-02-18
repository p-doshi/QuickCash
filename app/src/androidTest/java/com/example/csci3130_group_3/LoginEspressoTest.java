package com.example.csci3130_group_3;


import static android.app.PendingIntent.getActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static java.time.temporal.TemporalQueries.precision;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

public class LoginEspressoTest {
    public ActivityScenario<LoginActivity> scenario;


    @Before
    public void setup() {
        scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(activity -> {
           activity.setUpLoginButton();
        });
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
