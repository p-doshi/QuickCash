package com.example.csci3130_group_3;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.Matchers.allOf;
import static java.util.regex.Pattern.matches;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.csci3130_group_3.ChooseRoleActivity;


public class ChooseRoleEspressoTest {
    ActivityScenario<ChooseRoleActivity> activityScenario;
    @Before
    public void setup(){
        activityScenario=ActivityScenario.launch(ChooseRoleActivity.class);
        activityScenario.onActivity(activity -> {
        });
    }

    @Test
    public void employeeButtonExists() {
        onView(withId(R.id.employeeButton)).perform(click());
    }

    @Test
    public void employerButtonExists() {
        onView(withId(R.id.employerButton)).perform(click());
    }

    @Test
    public void roleConfirmationButtonExists() {
        onView(withId(R.id.chooseRoleConfirm)).perform(click());
    }


}
