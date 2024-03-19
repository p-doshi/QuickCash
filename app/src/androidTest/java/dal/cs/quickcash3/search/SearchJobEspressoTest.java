package dal.cs.quickcash3.search;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.Manifest;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.worker.WorkerDashboard;

public class SearchJobEspressoTest {

    @Rule
    public final ActivityScenarioRule<WorkerDashboard> activityRule =
            new ActivityScenarioRule<>(WorkerDashboard.class);
    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void searchResultsTest() {

        onView(withId(R.id.workerSearchPage)).perform(click());
        onView(allOf(withClassName(is("android.widget.ImageView")), withContentDescription("Search")))
                .perform(click());
        onView(withClassName(is("android.widget.SearchView$SearchAutoComplete")))
                .perform(replaceText("lawn"), pressImeActionButton());
        onView(withId(R.id.title)).check(matches(withText("Lawn Mowing")));

    }

}
