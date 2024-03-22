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

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;


import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.mock.MockDatabase;

@RunWith(AndroidJUnit4.class)
public class EmployerPostJobEspressoTest {
    private final Context context = ApplicationProvider.getApplicationContext();
    @Rule
    public final ActivityScenarioRule<EmployerDashboard> activityRule =
            new ActivityScenarioRule<>(
                    new Intent(context, EmployerDashboard.class)
                            .addCategory(context.getString(R.string.MOCK_DATABASE))
            );
    public ActivityScenario<EmployerDashboard> scenario;
    private String jobTitle;
    private String jobDate;
    private String jobDuration;
    private String jobUrgency;
    private String jobSalary;
    private String jobAddress;
    private String jobCity;
    private String jobProvince;
    private String jobDescription;
    private String empty;

    @Before
    public void setup() {
        scenario = activityRule.getScenario();
        jobTitle = "Mowing Lawn\n";
        jobDate = "15/03/2024\n";
        jobDuration = "1 – 2 Weeks";
        jobUrgency = "Low";
        jobSalary = "50\n";
        jobAddress = "1156 Wellington Street\n";
        jobCity = "Halifax\n";
        jobProvince = "NS";
        jobDescription = "Need a strong individual to help me mow my lawn because I am old.";
        empty = "\n";

        scenario.onActivity(activity ->
                // Do not run the test if we are not using the mock database.
                assertTrue("Not using Mock Database",
                        activity.getDatabase() instanceof MockDatabase)
        );


    }

    @Test
    public void fillJobForm() {

        onView(withId(R.id.jobPostingTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(typeText(jobDate), closeSoftKeyboard());
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.success)));
    }

    @Test
    public void checkEmptyJobTitleError(){
        onView(withId(R.id.jobPostingTitle)).perform(typeText(empty));
        onView(withId(R.id.addJobDate)).perform(typeText(jobDate), closeSoftKeyboard());
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidJobExpectedDurationError(){
        onView(withId(R.id.jobPostingTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(typeText(jobDate), closeSoftKeyboard());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidUrgencyError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(typeText(jobDate), closeSoftKeyboard());
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptySalaryError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(typeText(jobDate), closeSoftKeyboard());
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText(empty));
        onView(withId(R.id.addJobAddress)).perform(typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidSalaryError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(typeText(jobDate), closeSoftKeyboard());
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText("12.3365\n"));
        onView(withId(R.id.addJobAddress)).perform(typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.salaryError)));
    }

    @Test
    public void checkEmptyAddressError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(typeText(jobDate), closeSoftKeyboard());
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(typeText(empty));
        onView(withId(R.id.addJobCity)).perform(typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyCityError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(typeText(jobDate), closeSoftKeyboard());
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(typeText(empty));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidProvinceError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(typeText(jobDate), closeSoftKeyboard());
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(typeText(jobCity));
        onView(withId(R.id.addJobDescription)).perform(typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyJobDescriptionError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(typeText(jobDate), closeSoftKeyboard());
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText(empty),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyDateError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(typeText(empty), closeSoftKeyboard());
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidDateError() {
        onView(withId(R.id.jobPostingTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(typeText("15-03-2024\n"), closeSoftKeyboard());
        onView(withId(R.id.jobDurationSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(click());
        onView(withId(R.id.jobUrgencySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(click());
        onView(withId(R.id.addJobSalary)).perform(typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(click());
        onView(withId(R.id.addJobDescription)).perform(typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(click());

        onView(withId(R.id.jobSubmitStatus)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.dateError)));
    }
}
