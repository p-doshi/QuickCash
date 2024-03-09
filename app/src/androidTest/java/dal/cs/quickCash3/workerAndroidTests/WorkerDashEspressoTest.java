package dal.cs.quickCash3.workerAndroidTests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickCash3.R;
import dal.cs.quickCash3.worker.WorkerDashboard;

public class WorkerDashEspressoTest {
    @Rule
    public final ActivityScenarioRule<WorkerDashboard> activityRule =
        new ActivityScenarioRule<>(WorkerDashboard.class);

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
