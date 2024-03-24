package dal.cs.quickcash3.employer;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;

import static dal.cs.quickcash3.test.WaitForAction.waitFor;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
    public final ActivityScenarioRule<PostJobForm> activityRule =
            new ActivityScenarioRule<>(
                    new Intent(context, PostJobForm.class)
                            .addCategory(context.getString(R.string.MOCK_DATABASE))
            );
    private static final int MAX_TIMEOUT_MS = 15000;
    private String jobTitle;
    private String jobDate;
    private String jobDuration;
    private String jobUrgency;
    private String jobSalary;
    private String jobAddress;
    private String jobCity;
    private String jobProvince;
    private String jobDescription;

    @Before
    public void setup() {
        ActivityScenario<PostJobForm> scenario = activityRule.getScenario();
        jobTitle = "Mowing Lawn";
        jobDate = "15/03/2024";
        jobDuration = "1 – 2 Weeks";
        jobUrgency = "Low";
        jobSalary = "50";
        jobAddress = "1156 Wellington Street";
        jobCity = "Halifax";
        jobProvince = "NS";
        jobDescription = "Need a strong individual to help me mow my lawn because I am old.";

        scenario.onActivity(activity ->
                // Do not run the test if we are not using the mock database.
                assertTrue("Not using Mock Database",
                        activity.getDatabase() instanceof MockDatabase)
        );
    }

    @Test
    public void fillJobForm() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(jobDate));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(waitFor(withText(R.string.success), MAX_TIMEOUT_MS));
    }

    @Test
    public void checkEmptyJobTitleError(){
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(jobDate));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidJobExpectedDurationError(){
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(jobDate));
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidUrgencyError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(jobDate));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptySalaryError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(jobDate));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(scrollTo(),click());
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidSalaryError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(jobDate));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText("12.3365"));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.salaryError)));
    }

    @Test
    public void checkEmptyAddressError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(jobDate));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(jobSalary));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyCityError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(jobDate));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(jobAddress));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidProvinceError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(jobDate));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(jobCity));
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyJobDescriptionError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(jobDate));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(scrollTo(),click(),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyDateError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(jobTitle));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidDateError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(jobTitle));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText("15-03-2024"));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobDuration))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobUrgency))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(jobSalary));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(jobAddress));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(jobCity));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(jobProvince))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(jobDescription),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.dateError)));
    }
}
