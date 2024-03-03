package com.example.csci3130_group_3;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
public class WorkerDashEspressoTest {
    public ActivityScenario<WorkerDashboard> scenario;

    @Before
    public void setup(){
        scenario = ActivityScenario.launch(WorkerDashboard.class);
        scenario.onActivity(activity -> {

        });
    }

    @Test
    public void testWorkerNavBarExist(){
        onView(withId(R.id.workerBottomNavView)).perform(click());
    }

    @Test
    public void testWorkerSearchButtonExist(){
        onView(withId(R.id.workerSearchPage)).check(matches(isDisplayed()));
    }

    @Test
    public void testWorkerMapButtonExist(){
        onView(withId(R.id.workerMapPage)).check(matches(isDisplayed()));
    }

    @Test
    public void testWorkerReceiptButtonExist(){
        onView(withId(R.id.workerReceiptPage)).check(matches(isDisplayed()));
    }

    @Test
    public void testWorkerProfileButtonExist(){
        onView(withId(R.id.workerProfilePage)).check(matches(isDisplayed()));
    }
}
