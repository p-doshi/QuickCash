package com.example.csci3130_group_3;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.csci3130_group_3.RegexMatcher.withPattern;

import android.Manifest;
import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LocationEspressoTests {
    @Rule
    public GrantPermissionRule permissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public ActivityScenarioRule<LocationExampleActivity> activityRule =
        new ActivityScenarioRule<>(LocationExampleActivity.class);
    private final Context context = getInstrumentation().getContext();

    @Test
    public void checkIfElementsVisible() {
        Espresso.onView(withId(R.id.locationStatus)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.detectButton)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.latText)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.longText)).check(matches(isDisplayed()));
    }

    @Test
    public void canGetLocation() {
        Espresso.onView(withId(R.id.detectButton)).perform(click());
        Espresso.onView(withId(R.id.locationStatus)).check(matches(withPattern(".*Granted")));
        Espresso.onView(withId(R.id.longText)).check(matches(withPattern("Longitude: [-\\d.]+")));
        Espresso.onView(withId(R.id.latText)).check(matches(withPattern("Latitude: [-\\d.]+")));
    }
}
