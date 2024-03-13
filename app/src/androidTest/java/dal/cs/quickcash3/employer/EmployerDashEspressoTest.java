package dal.cs.quickcash3.employer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Before;
import org.junit.Test;

import dal.cs.quickcash3.R;

public class EmployerDashEspressoTest {
    public ActivityScenario<EmployerDashboard> scenario;

    @Before
    public void setup(){
        scenario = ActivityScenario.launch(EmployerDashboard.class);
        scenario.onActivity(activity -> {

        });
    }

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
