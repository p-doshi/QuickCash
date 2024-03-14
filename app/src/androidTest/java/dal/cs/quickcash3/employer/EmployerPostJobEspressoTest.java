package dal.cs.quickcash3.employer;

import androidx.test.core.app.ActivityScenario;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.allOf;


import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dal.cs.quickcash3.R;

@RunWith(AndroidJUnit4.class)
public class EmployerPostJobEspressoTest {
    public ActivityScenario<PostJobForm> scenario;

    public Context context;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(PostJobForm.class);
        scenario.onActivity(activity -> {
            context = activity;
        });
    }

    @Test
    public void fillJobForm() {

        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(PickerActions.setDate(2017, 6, 30));
        onView(withId(R.id.addJobDuration)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2 - 4 Weeks"))).perform(click());
        onView(withId(R.id.addJobUrgency)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Urgent"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old.\n"),closeSoftKeyboard());

        onView(withId(R.id.addJobConfimButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText("Job submitted successfully")));
    }

    @Test
    public void checkInvalidJobTitleError(){
        onView(withId(R.id.jobPostingTitle)).perform(typeText("\n"));
        onView(withId(R.id.addJobDate)).perform(PickerActions.setDate(2017, 6, 30));
        onView(withId(R.id.addJobDuration)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2 - 4 Weeks"))).perform(click());
        onView(withId(R.id.addJobUrgency)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Urgent"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old.\n"),closeSoftKeyboard());

        onView(withId(R.id.addJobConfimButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid Title")));
    }

    @Test
    public void checkInvalidJobExpectedDurationError(){
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(PickerActions.setDate(2017, 6, 30));
        onView(withId(R.id.addJobUrgency)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Urgent"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old.\n"),closeSoftKeyboard());

        onView(withId(R.id.addJobConfimButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid Job Expected Duration")));
    }

    @Test
    public void checkInvalidUrgencyError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(PickerActions.setDate(2017, 6, 30));
        onView(withId(R.id.addJobDuration)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2 - 4 Weeks"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old.\n"),closeSoftKeyboard());

        onView(withId(R.id.addJobConfimButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid Urgency")));
    }

    @Test
    public void checkInvalidSalaryError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(PickerActions.setDate(2017, 6, 30));
        onView(withId(R.id.addJobDuration)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2 - 4 Weeks"))).perform(click());
        onView(withId(R.id.addJobUrgency)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Urgent"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("abc\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old.\n"),closeSoftKeyboard());

        onView(withId(R.id.addJobConfimButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid Salary")));
    }

    @Test
    public void checkInvalidAddressError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(PickerActions.setDate(2017, 6, 30));
        onView(withId(R.id.addJobDuration)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2 - 4 Weeks"))).perform(click());
        onView(withId(R.id.addJobUrgency)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Urgent"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old.\n"),closeSoftKeyboard());

        onView(withId(R.id.addJobConfimButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid Address")));
    }

    @Test
    public void checkInvalidCityError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(PickerActions.setDate(2017, 6, 30));
        onView(withId(R.id.addJobDuration)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2 - 4 Weeks"))).perform(click());
        onView(withId(R.id.addJobUrgency)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Urgent"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old.\n"),closeSoftKeyboard());

        onView(withId(R.id.addJobConfimButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid City")));
    }

    @Test
    public void checkInvalidProvinceError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(PickerActions.setDate(2017, 6, 30));
        onView(withId(R.id.addJobDuration)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2 - 4 Weeks"))).perform(click());
        onView(withId(R.id.addJobUrgency)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Urgent"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old.\n"),closeSoftKeyboard());

        onView(withId(R.id.addJobConfimButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid Province")));
    }

    @Test
    public void checkInvalidJobDescriptionError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(PickerActions.setDate(2017, 6, 30));
        onView(withId(R.id.addJobDuration)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2 - 4 Weeks"))).perform(click());
        onView(withId(R.id.addJobUrgency)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Urgent"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("\n"),closeSoftKeyboard());

        onView(withId(R.id.addJobConfimButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid Job Description")));
    }

    @Test
    public void checkInvalidDateError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDuration)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2 - 4 Weeks"))).perform(click());
        onView(withId(R.id.addJobUrgency)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Urgent"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("\n"),closeSoftKeyboard());

        onView(withId(R.id.addJobConfimButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid Date")));
    }
}
