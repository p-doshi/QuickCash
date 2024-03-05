package com.example.csci3130_group_3;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.csci3130_group_3.RegexMatcher.withPattern;

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
    @Rule
    public ActivityScenarioRule<LocationExampleActivity> activityRule =
        new ActivityScenarioRule<>(LocationExampleActivity.class);
    @Rule
    public GrantPermissionRule permissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void checkIfElementsVisible() {
        Espresso.onView(withId(R.id.status)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.detectButton)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.latText)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.longText)).check(matches(isDisplayed()));
    }

    @Test
    public void canGetLocation() {
        Espresso.onView(withId(R.id.detectButton)).perform(click());
        Espresso.onView(withId(R.id.longText)).check(matches(withText("Longitude: PENDING...")));
        Espresso.onView(withId(R.id.latText)).check(matches(withText("Latitude: PENDING...")));

        Espresso.onView(withId(R.id.detectButton)).perform(click());
        Espresso.onView(withId(R.id.longText)).check(matches(withPattern("Longitude: [-\\d.]+")));
        Espresso.onView(withId(R.id.latText)).check(matches(withPattern("Latitude: [-\\d.]+")));
    }
}
