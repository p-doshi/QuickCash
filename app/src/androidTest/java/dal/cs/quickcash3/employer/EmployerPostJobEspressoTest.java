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

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.geocode.MockGeocoder;

@RunWith(AndroidJUnit4.class)
public class EmployerPostJobEspressoTest {
    private final Context context = ApplicationProvider.getApplicationContext();
    @Rule
    public final ActivityScenarioRule<EmployerDashboard> activityRule =
        new ActivityScenarioRule<>(
            new Intent(context, EmployerDashboard.class)
                .addCategory(context.getString(R.string.MOCK_DATABASE))
                .addCategory(context.getString(R.string.MOCK_GEOCODER)));
    private static final String JOB_TITLE = "Mowing Lawn";
    private static final String JOB_DATE = "15/03/2024";
    private static final String JOB_DURATION = "1 – 2 Weeks";
    private static final String JOB_URGENCY = "Low";
    private static final String JOB_SALARY = "50";
    private static final String JOB_ADDRESS = "1156 Wellington Street";
    private static final String JOB_CITY = "Halifax";
    private static final String JOB_PROVINCE = "NS";
    private static final String JOB_DESCRIPTION = "Need a strong individual to help me mow my lawn because I am old.";
    private MockGeocoder geocoder;


    @Before
    public void setup() {
        ActivityScenario<EmployerDashboard> scenario = activityRule.getScenario();

        scenario.onActivity(activity ->{
            // Do not run the test if we are not using the mock database.
            assertTrue("Not using Mock Database",
                            activity.getDatabase() instanceof MockDatabase);
            assertTrue("Not using Mock Geocoder", activity.getGeocoder() instanceof MockGeocoder);

            geocoder = (MockGeocoder) activity.getGeocoder();
        });



    }

    @Test
    public void fillJobForm() {
        geocoder.addAddressMatcher(Pattern.compile(".+"), new LatLng(0.0, 0.0));

        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(JOB_TITLE));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(JOB_DATE));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_DURATION))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_URGENCY))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(JOB_SALARY));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(JOB_ADDRESS));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(JOB_CITY));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_PROVINCE))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(JOB_DESCRIPTION),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).check(matches(withText(R.string.success)));
    }

    @Test
    public void checkEmptyJobTitleError(){
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(JOB_DATE));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_DURATION))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_URGENCY))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(JOB_SALARY));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(JOB_ADDRESS));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(JOB_CITY));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_PROVINCE))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(JOB_DESCRIPTION),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidJobExpectedDurationError(){
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(JOB_TITLE));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(JOB_DATE));
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_URGENCY))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(JOB_SALARY));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(JOB_ADDRESS));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(JOB_CITY));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_PROVINCE))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(JOB_DESCRIPTION),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidUrgencyError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(JOB_TITLE));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(JOB_DATE));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_DURATION))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(JOB_SALARY));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(JOB_ADDRESS));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(JOB_CITY));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_PROVINCE))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(JOB_DESCRIPTION),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptySalaryError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(JOB_TITLE));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(JOB_DATE));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_DURATION))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_URGENCY))).perform(scrollTo(),click());
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(JOB_ADDRESS));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(JOB_CITY));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_PROVINCE))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(JOB_DESCRIPTION),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidSalaryError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(JOB_TITLE));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(JOB_DATE));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_DURATION))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_URGENCY))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText("12.3365"));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(JOB_ADDRESS));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(JOB_CITY));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_PROVINCE))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(JOB_DESCRIPTION),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.salaryError)));
    }

    @Test
    public void checkEmptyAddressError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(JOB_TITLE));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(JOB_DATE));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_DURATION))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_URGENCY))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(JOB_SALARY));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(JOB_CITY));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_PROVINCE))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(JOB_DESCRIPTION),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyCityError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(JOB_TITLE));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(JOB_DATE));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_DURATION))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_URGENCY))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(JOB_SALARY));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(JOB_ADDRESS));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_PROVINCE))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(JOB_DESCRIPTION),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidProvinceError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(JOB_TITLE));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(JOB_DATE));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_DURATION))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_URGENCY))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(JOB_SALARY));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(JOB_ADDRESS));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(JOB_CITY));
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(JOB_DESCRIPTION),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyJobDescriptionError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(JOB_TITLE));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText(JOB_DATE));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_DURATION))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_URGENCY))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(JOB_SALARY));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(JOB_ADDRESS));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(JOB_CITY));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_PROVINCE))).perform(scrollTo(),click(),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkEmptyDateError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(JOB_TITLE));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_DURATION))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_URGENCY))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(JOB_SALARY));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(JOB_ADDRESS));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(JOB_CITY));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_PROVINCE))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(JOB_DESCRIPTION),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.fillAllFields)));
    }

    @Test
    public void checkInvalidDateError() {
        onView(withId(R.id.jobPostingTitle)).perform(scrollTo(),typeText(JOB_TITLE));
        onView(withId(R.id.addJobDate)).perform(scrollTo(),typeText("15-03-2024"));
        onView(withId(R.id.jobDurationSpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_DURATION))).perform(scrollTo(),click());
        onView(withId(R.id.jobUrgencySpinner)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_URGENCY))).perform(scrollTo(),click());
        onView(withId(R.id.addJobSalary)).perform(scrollTo(),typeText(JOB_SALARY));
        onView(withId(R.id.addJobAddress)).perform(scrollTo(),typeText(JOB_ADDRESS));
        onView(withId(R.id.addJobCity)).perform(scrollTo(),typeText(JOB_CITY));
        onView(withId(R.id.addJobProvince)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is(JOB_PROVINCE))).perform(scrollTo(),click());
        onView(withId(R.id.addJobDescription)).perform(scrollTo(),typeText(JOB_DESCRIPTION),closeSoftKeyboard());

        onView(withId(R.id.addJobConfirmButton)).perform(scrollTo(),click());

        onView(withId(R.id.jobSubmitStatus)).perform(scrollTo()).check(matches(withText(R.string.dateError)));
    }
}
