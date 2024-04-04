package dal.cs.quickcash3.worker;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static dal.cs.quickcash3.test.ExampleJobList.JOBS;
import static dal.cs.quickcash3.test.RangeSliderSwiper.adjustRangeSliderThumbs;
import static dal.cs.quickcash3.test.RecyclerViewItemCountMatcher.recyclerHasItemCount;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;

public class PreferencesEspressoTest {
    Context context = getInstrumentation().getTargetContext();
    private final double MIN = 0.0;
    private final double MAX = Double.POSITIVE_INFINITY;
    private final double MID_DURATION = 24.0;
    private final double MID_SALARY = 60.0;
    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
    @Rule
    public final ActivityScenarioRule<StorePreferencesActivity> activityRule =
            new ActivityScenarioRule<>(StorePreferencesActivity.class);

    private void checkJobPosts(@NonNull List<String> expectedJobTitles) {
        onView(withId(R.id.jobListRecyclerView)).check(matches(recyclerHasItemCount(expectedJobTitles.size())));

        for (AvailableJob job : JOBS.values()) {
            if (expectedJobTitles.contains(job.getTitle())) {
                onView(allOf(withId(R.id.title), withText(job.getTitle())));
                onView(allOf(withId(R.id.subhead), withText(job.getDescription())));
            }
        }
    }

    @Test
    public void minDurationPreferenceTest() {
        onView(withId(R.id.preferenceDurationRangeSlider)).perform(adjustRangeSliderThumbs(0.0f, 1.0f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        Assert.assertEquals(MIN,sharedPreferences.getFloat(context.getString(R.string.time_min), -1.0f),0);
    }
    @Test
    public void maxDurationPreferenceTest() {
        onView(withId(R.id.preferenceDurationRangeSlider)).perform(adjustRangeSliderThumbs(0.0f, 1.0f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        Assert.assertEquals(MAX,sharedPreferences.getFloat(context.getString(R.string.time_max), -1.0f),0);
    }
    @Test
    public void greaterThanOneDayPreferenceTest() {
        onView(withId(R.id.preferenceDurationRangeSlider)).perform(adjustRangeSliderThumbs(0.5f, 1.0f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        Assert.assertEquals(MID_DURATION,sharedPreferences.getFloat(context.getString(R.string.time_min), -1.0f),0);
    }
    @Test
    public void minSalaryPreferenceTest() {
        onView(withId(R.id.preferenceSalaryRangeSlider)).perform(adjustRangeSliderThumbs(0.0f, 1.0f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        Assert.assertEquals(MIN,sharedPreferences.getFloat(context.getString(R.string.salary_min), -1.0f),0);
    }
    @Test
    public void maxSalaryPreferenceTest() {
        onView(withId(R.id.preferenceSalaryRangeSlider)).perform(adjustRangeSliderThumbs(0.0f, 1.0f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        Assert.assertEquals(MAX,sharedPreferences.getFloat(context.getString(R.string.salary_max), -1.0f),0);
    }

    @Test
    public void lessThanMaxPayPreferenceTest() {
        onView(withId(R.id.preferenceSalaryRangeSlider)).perform(adjustRangeSliderThumbs(0.0f, 0.5f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        Assert.assertEquals(MID_SALARY,sharedPreferences.getFloat(context.getString(R.string.salary_max), -1.0f),0);
    }

}
