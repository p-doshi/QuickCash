package dal.cs.quickcash3.employer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import dal.cs.quickcash3.R;

public class EmployerJobHistoryPageEspressoTest {
    @Rule
    public final ActivityScenarioRule<EmployerJobHistoryPage> activityRule =
            new ActivityScenarioRule<>(EmployerJobHistoryPage.class);

    static JobHistory jobHistory1;
    static JobHistory jobHistory2;
    static JobHistory jobHistory3;

    @BeforeClass
    public static void setup() {
        //Incomplete setup method, mock all the required dependencies!
        jobHistory1 = Mockito.mock(JobHistory.class);
        jobHistory2 = Mockito.mock(JobHistory.class);
        jobHistory3 = Mockito.mock(JobHistory.class);

        Mockito.when(jobHistory1.getJobName()).thenReturn("Job 1");
        Mockito.when(jobHistory1.getIncome()).thenReturn("1000");
        Mockito.when(jobHistory1.getReputation()).thenReturn("Good");

        Mockito.when(jobHistory2.getJobName()).thenReturn("Job 2");
        Mockito.when(jobHistory2.getIncome()).thenReturn("1500");
        Mockito.when(jobHistory2.getReputation()).thenReturn("Excellent");

        Mockito.when(jobHistory3.getJobName()).thenReturn("Job 3");
        Mockito.when(jobHistory3.getIncome()).thenReturn("2000");
        Mockito.when(jobHistory3.getReputation()).thenReturn("Average");

    }

    @Test
    public void emptyJobHistoryPage() {

        onView(withId(R.id.totalJobs)).check(matches(withText("0")));
        onView(withId(R.id.totalIncome)).check(matches(withText("0")));
        onView(withId(R.id.averageReputation)).check(matches(withText("0")));
    }
    @Test
    public void fillJobHistoryPage() {
        onView(withId(R.id.jobHistory1Name)).check(matches(withText("Job 1")));
        onView(withId(R.id.jobHistory1Income)).check(matches(withText("1000")));
        onView(withId(R.id.jobHistory1Reputation)).check(matches(withText("4")));

        onView(withId(R.id.jobHistory2Name)).check(matches(withText("Job 2")));
        onView(withId(R.id.jobHistory2Income)).check(matches(withText("1500")));
        onView(withId(R.id.jobHistory2Reputation)).check(matches(withText("5")));

        onView(withId(R.id.jobHistory3Name)).check(matches(withText("Job 3")));
        onView(withId(R.id.jobHistory3Income)).check(matches(withText("2000")));
        onView(withId(R.id.jobHistory3Reputation)).check(matches(withText("3")));


        onView(withId(R.id.totalJobs)).check(matches(withText("3")));
        onView(withId(R.id.totalIncome)).check(matches(withText("4500")));
        onView(withId(R.id.averageReputation)).check(matches(withText("4")));
    }
}
