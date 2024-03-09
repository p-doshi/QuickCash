package dal.cs.quickCash3.employerAndroidTests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickCash3.R;
import dal.cs.quickCash3.employer.EmployerDashboard;

public class EmployerDashEspressoTest {
    @Rule
    public final ActivityScenarioRule<EmployerDashboard> activityRule =
        new ActivityScenarioRule<>(EmployerDashboard.class);

    @Test
    public void testEmployerNavBarExist(){
        onView(ViewMatchers.withId(R.id.employerBottomNavView)).perform(click());
    }

    @Test
    public void testEmployerAddJobButtonExist(){
        onView(withId(R.id.employer_add_job)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmployerReceiptsButtonExist(){
        onView(withId(R.id.employer_reciepts)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmployerProfileButtonExist(){
        onView(withId(R.id.employer_profile)).check(matches(isDisplayed()));
    }
}
