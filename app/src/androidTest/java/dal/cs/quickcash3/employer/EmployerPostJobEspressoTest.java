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
        onView(withId(R.id.addJobDate)).perform(typeText("15/03/2024\n"));
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 – 2 Weeks"))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Low"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old."),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.success)));
    }

    @Test
    public void checkEmptyJobTitleError(){
        onView(withId(R.id.jobPostingTitle)).perform(typeText("\n"));
        onView(withId(R.id.addJobDate)).perform(typeText("15/03/2024\n"));
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 – 2 Weeks"))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Low"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old."),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidJobExpectedDurationError(){
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(typeText("15/03/2024\n"));
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Low"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old."),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidUrgencyError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(typeText("15/03/2024\n"));
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 – 2 Weeks"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old."),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptySalaryError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(typeText("15/03/2024\n"));
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 – 2 Weeks"))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Low"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("abc\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old."),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidSalaryError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(typeText("15/03/2024\n"));
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 – 2 Weeks"))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Low"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("12.3365\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old."),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.salaryError)));
    }

    @Test
    public void checkEmptyAddressError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(typeText("15/03/2024\n"));
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 – 2 Weeks"))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Low"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old."),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyCityError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(typeText("15/03/2024\n"));
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 – 2 Weeks"))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Low"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old."),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidProvinceError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(typeText("15/03/2024\n"));
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 – 2 Weeks"))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Low"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old."),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyJobDescriptionError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(typeText("15/03/2024\n"));
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 – 2 Weeks"))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Low"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("\n"),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyDateError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 – 2 Weeks"))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Low"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old."),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidDateError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText("Mowing Lawn\n"));
        onView(withId(R.id.addJobDate)).perform(typeText("15-03-2024\n"));
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1 – 2 Weeks"))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Low"))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("50\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText("1156 Wellington Street\n"));
        onView(withId(R.id.addJobCity)).perform(typeText("Halifax\n"));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NS"))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText("Need a strong individual to help me mow my lawn because I am old."),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.dateError)));
    }
}
