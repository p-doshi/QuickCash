package dal.cs.quickcash3.database;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;

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

@RunWith(AndroidJUnit4.class)
public class MockDatabasePageTest {
    private final Context context = ApplicationProvider.getApplicationContext();
    @Rule
    public final ActivityScenarioRule<DatabaseExampleActivity> activityRule =
        new ActivityScenarioRule<>(
            new Intent(context, DatabaseExampleActivity.class)
                .addCategory(context.getString(R.string.MOCK_DATABASE))
        );

    @Before
    public void setup() {
        ActivityScenario<DatabaseExampleActivity> scenario = activityRule.getScenario();
        scenario.onActivity(activity ->
            // Do not run the test if we are not using the mock database.
            assertTrue("Not using Mock Database",
                activity.getDatabase() instanceof MockDatabase)
        );
    }

    @Test
    public void write() {
        onView(withId(R.id.writeButton)).perform(click());
        onView(withId(R.id.dbOutput)).check(matches(withText(containsString(context.getString(R.string.db_write)))));
    }

    @Test
    public void read() {
        onView(withId(R.id.readButton)).perform(click());
        onView(withId(R.id.dbOutput)).check(matches(withText(containsString(context.getString(R.string.db_error_reading)))));
    }

    @Test
    public void writeRead() {
        onView(withId(R.id.writeButton)).perform(click());
        onView(withId(R.id.dbOutput)).check(matches(withText(containsString(context.getString(R.string.db_write)))));
        onView(withId(R.id.readButton)).perform(click());
        onView(withId(R.id.dbOutput)).check(matches(withText(containsString(context.getString(R.string.db_read)))));
    }
}
