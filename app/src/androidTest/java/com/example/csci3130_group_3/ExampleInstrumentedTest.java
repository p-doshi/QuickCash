package com.example.csci3130_group_3;


import static android.app.PendingIntent.getActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public ActivityScenario<MainActivity> scenario;
    public View decorView;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity((ActivityScenario.ActivityAction<MainActivity>) activity -> {
            decorView = activity.getWindow().getDecorView();
        });
    }

    @Test
    public void testValidCredentials() {
        onView(withId(R.id.emailaddress)).perform(typeText("5")).perform(closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("hi")).perform(closeSoftKeyboard());
        onView(withId(R.id.Continue)).perform(click());
        onView(withText(R.string.TOAST_STRING))
            .inRoot(withDecorView(not(is(decorView))))
            .check(matches(isDisplayed()));
    }
}
