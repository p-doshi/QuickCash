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

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class LoginEspressoTest {
    public ActivityScenario<LoginActivity> scenario;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(activity -> {
           activity.setUpLoginButton();
        });
    }

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
    }

    @Test
    public void testInvalidEmail() {
        onView(withId(R.id.emailaddress)).perform(typeText("pdoshigmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("hahapranked")).perform(closeSoftKeyboard());
        onView(withId(R.id.continueButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.INVALID_EMAIL_TOAST)));
    }

}
