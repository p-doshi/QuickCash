package dal.cs.quickcash3.employer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.JOBS;
import static dal.cs.quickcash3.test.ExampleJobList.generateJobPosts;
import static dal.cs.quickcash3.test.ExampleUserList.EMPLOYER1;
import static dal.cs.quickcash3.test.ExampleUserList.generateUsers;
import static dal.cs.quickcash3.test.RecyclerViewItemCountMatcher.recyclerHasItemCount;
import static dal.cs.quickcash3.test.SiblingMatcher.withSibling;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;

@RunWith(AndroidJUnit4.class)
public class EmployerApplicantsEspressoTest {
    private final Context context = ApplicationProvider.getApplicationContext();
    @Rule
    public final ActivityScenarioRule<EmployerDashboard> activityRule =
        new ActivityScenarioRule<>(
            new Intent(context, EmployerDashboard.class)
                .addCategory(context.getString(R.string.MOCK_DATABASE))
                .putExtra("user", EMPLOYER1));

    private Database database;

    private void checkJobPosts(@NonNull List<String> expectedJobTitles) {
        onView(withId(R.id.jobListRecyclerView)).check(matches(recyclerHasItemCount(expectedJobTitles.size())));

        for (AvailableJob job : JOBS.values()) {
            if (expectedJobTitles.contains(job.getTitle())) {
                onView(allOf(withId(R.id.title), withText(job.getTitle())));
                onView(allOf(withId(R.id.subhead), withText(job.getDescription())));
            }
        }
    }

    private void checkApplicants(@NonNull List<String> expectedApplicants) {
        onView(withId(R.id.applicantsRecyclerView)).check(matches(recyclerHasItemCount(expectedApplicants.size())));

        for (String applicant : expectedApplicants) {
            onView(allOf(isDescendantOfA(withId(R.id.applicantsRecyclerView)), withId(R.id.worker), withText(applicant)));
        }
    }

    private void checkRejected(@NonNull List<String> expectedRejected) {
        onView(withId(R.id.rejectedRecyclerView)).check(matches(recyclerHasItemCount(expectedRejected.size())));

        for (String rejected : expectedRejected) {
            onView(allOf(isDescendantOfA(withId(R.id.rejectedRecyclerView)), withId(R.id.worker), withText(rejected)));
        }
    }

    @Before
    public void setup() throws Throwable {
        ActivityScenario<EmployerDashboard> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Do not run the test if we are not using the mock implementation.
            assertTrue("Not using Mock Database", activity.getDatabase() instanceof MockDatabase);

            database = activity.getDatabase();
        });

        runOnUiThread(() -> generateJobPosts(database, Assert::fail));
        runOnUiThread(() -> generateUsers(database, Assert::fail));
    }

    @Test
    public void checkListings() {
        List<String> expectedJobTitles = Arrays.asList(
            "Walk Dog",
            "Coding problem"
        );

        checkJobPosts(expectedJobTitles);
    }

    @Test
    public void rejectAll() {
        onView(allOf(withId(R.id.title), withText("Coding problem"))).perform(scrollTo(), click());
        onView(withId(R.id.applicantsRecyclerView)).perform(scrollTo());
        onView(allOf(withId(R.id.rejectButton),
            withSibling(allOf(withId(R.id.worker), withText("Ethan Rozee")))))
            .perform(scrollTo(), click());
        onView(allOf(withId(R.id.rejectButton),
            withSibling(allOf(withId(R.id.worker), withText("Hayley Vezeau")))))
            .perform(scrollTo(), click());

        List<String> expectedApplicants = new ArrayList<>();

        checkApplicants(expectedApplicants);

        List<String> expectedRejected = Arrays.asList(
            "Ethan Rozee",
            "Hayley Vezeau"
        );

        checkRejected(expectedRejected);
    }

    @Test
    public void rejectUndo() {
        onView(allOf(withId(R.id.title), withText("Coding problem"))).perform(scrollTo(), click());
        onView(withId(R.id.applicantsRecyclerView)).perform(scrollTo());
        onView(allOf(withId(R.id.rejectButton),
            withSibling(allOf(withId(R.id.worker), withText("Ethan Rozee")))))
            .perform(scrollTo(), click());
        onView(withId(R.id.rejectedRecyclerView)).perform(scrollTo());
        onView(allOf(withId(R.id.undoButton),
            withSibling(allOf(withId(R.id.worker), withText("Ethan Rozee")))))
            .perform(scrollTo(), click());

        List<String> expectedApplicants = Arrays.asList(
            "Ethan Rozee",
            "Hayley Vezeau"
        );

        checkApplicants(expectedApplicants);

        List<String> expectedRejected = new ArrayList<>();

        checkRejected(expectedRejected);
    }

    @Test
    public void rejectRefreshUndo() {
        onView(allOf(withId(R.id.title), withText("Coding problem"))).perform(scrollTo(), click());
        onView(withId(R.id.applicantsRecyclerView)).perform(scrollTo());
        onView(allOf(withId(R.id.rejectButton),
            withSibling(allOf(withId(R.id.worker), withText("Ethan Rozee")))))
            .perform(scrollTo(), click());

        pressBack();

        onView(allOf(withId(R.id.title), withText("Coding problem"))).perform(scrollTo(), click());
        onView(withId(R.id.rejectedRecyclerView)).perform(scrollTo());
        onView(allOf(withId(R.id.undoButton),
            withSibling(allOf(withId(R.id.worker), withText("Ethan Rozee")))))
            .perform(scrollTo(), click());

        List<String> expectedApplicants = Arrays.asList(
            "Ethan Rozee",
            "Hayley Vezeau"
        );

        checkApplicants(expectedApplicants);

        List<String> expectedRejected = new ArrayList<>();

        checkRejected(expectedRejected);
    }
}
