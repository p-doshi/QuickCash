package com.example.csci3130_group_3;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.csci3130_group_3.EmployeeDashboard;


public class EmployeeDashEspresso {
    public ActivityScenario<EmployeeDashboard> scenario;

    @Before
    public void setup(){
        scenario = ActivityScenario.launch(EmployeeDashboard.class);
        scenario.onActivity(activity -> {

        });
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
