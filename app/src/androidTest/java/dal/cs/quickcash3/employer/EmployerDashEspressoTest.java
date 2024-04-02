package dal.cs.quickcash3.employer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.R;

public class EmployerDashEspressoTest {
    @Rule
    public final ActivityScenarioRule<EmployerDashboard> activityRule =
        new ActivityScenarioRule<>(EmployerDashboard.class);

    @Test
    public void testEmployerNavBarExist(){
        onView(withId(R.id.employerBottomNavView)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmployerAddJobButtonExist(){
        onView(withId(R.id.employer_job_listings)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmployerReceiptsButtonExist(){
        onView(withId(R.id.employer_history)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmployerProfileButtonExist(){
        onView(withId(R.id.employer_profile)).check(matches(isDisplayed()));
    }
}
