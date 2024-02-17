package com.example.csci3130_group_3;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
public class EmployeeDashEspressoTest {
    public ActivityScenario<EmployeeDashboard> scenario;

    @Before
    public void setup(){
        scenario = ActivityScenario.launch(EmployeeDashboard.class);
        scenario.onActivity(activity -> {

        });
    }

    @Test
    public void testEmployeeNavBarExist(){
        onView(withId(R.id.employeeBottomNavView)).perform(click());
    }

    @Test
    public void testEmployeeSearchButtonExist(){
        onView(withId(R.id.employeeSearchPage)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmployeeMapButtonExist(){
        onView(withId(R.id.employeeMapPage)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmployeeReceiptButtonExist(){
        onView(withId(R.id.employeeReceiptPage)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmployeeProfileButtonExist(){
        onView(withId(R.id.employeeProfilePage)).check(matches(isDisplayed()));
    }
}
