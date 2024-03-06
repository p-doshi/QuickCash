package com.example.csci3130_group_3;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.example.csci3130_group_3.RegexMatcher.withPattern;
import static com.example.csci3130_group_3.WaitForAction.waitFor;

import android.Manifest;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LocationEspressoTests {
    private static final long MAX_LOCATION_TIMEOUT_MS = 20000;
    @Rule
    public GrantPermissionRule permissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public ActivityScenarioRule<LocationExampleActivity> activityRule =
        new ActivityScenarioRule<>(LocationExampleActivity.class);

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
        Espresso.onView(withId(R.id.locationStatus)).perform(waitFor(withPattern(".*Granted"), MAX_LOCATION_TIMEOUT_MS));
        Espresso.onView(withId(R.id.longText)).check(matches(withPattern("Longitude: [-\\d.]+")));
        Espresso.onView(withId(R.id.latText)).check(matches(withPattern("Latitude: [-\\d.]+")));
    }
}
