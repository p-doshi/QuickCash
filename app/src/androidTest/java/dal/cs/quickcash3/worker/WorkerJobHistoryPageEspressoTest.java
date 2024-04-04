package dal.cs.quickcash3.worker;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.R;

public class WorkerJobHistoryPageEspressoTest {
    @Rule
    public final ActivityScenarioRule<WorkHistoryActivity> activityRule =
            new ActivityScenarioRule<>(WorkHistoryActivity.class);

    @Test
    public void fillJobHistoryPage() {
        int baseId = 10000;
        onView(withId(baseId)).check(matches(withText("delivery")));
        onView(withId(baseId + 1)).check(matches(withText("Income: 100.00")));
        onView(withId(baseId + 2)).check(matches(withText("Reputation: 4.0")));

        onView(withId(baseId + 3)).check(matches(withText("take care")));
        onView(withId(baseId + 4)).check(matches(withText("Income: 200.00")));
        onView(withId(baseId + 5)).check(matches(withText("Reputation: 5.0")));

        onView(withId(baseId + 6)).check(matches(withText("take care")));
        onView(withId(baseId + 7)).check(matches(withText("Income: 200.00")));
        onView(withId(baseId + 8)).check(matches(withText("Reputation: 3.0")));


        onView(withId(R.id.totalIncome)).check(matches(withText("Total Income: 500.00")));
        onView(withId(R.id.averageReputation)).check(matches(withText("Average Reputation: 4.00")));
    }
}
