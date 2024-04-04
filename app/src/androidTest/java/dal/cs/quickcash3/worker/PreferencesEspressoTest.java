package dal.cs.quickcash3.worker;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static dal.cs.quickcash3.test.RangeSliderSwiper.adjustRangeSliderThumbs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.R;

public class PreferencesEspressoTest {
    Context context = getInstrumentation().getTargetContext();
    private final double min = 0.0;
    private final double max = Double.POSITIVE_INFINITY;
    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
    @Rule
    public final ActivityScenarioRule<StorePreferencesActivity> activityRule =
            new ActivityScenarioRule<>(StorePreferencesActivity.class);

    @Test
    public void minDurationPreferenceTest() {
        onView(withId(R.id.preferenceDurationRangeSlider)).perform(adjustRangeSliderThumbs(0.0f, 1.0f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        Assert.assertEquals(min,sharedPreferences.getFloat(context.getString(R.string.time_min), -1.0f),0);
    }
    @Test
    public void maxDurationPreferenceTest() {
        onView(withId(R.id.preferenceDurationRangeSlider)).perform(adjustRangeSliderThumbs(0.0f, 1.0f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        Assert.assertEquals(max,sharedPreferences.getFloat(context.getString(R.string.time_max), -1.0f),0);
    }
    @Test
    public void greaterThanOneDayPreferenceTest() {
        onView(withId(R.id.preferenceDurationRangeSlider)).perform(adjustRangeSliderThumbs(0.5f, 1.0f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        double midDuration = 24.0;
        Assert.assertEquals(midDuration,sharedPreferences.getFloat(context.getString(R.string.time_min), -1.0f),0);
    }
    @Test
    public void minSalaryPreferenceTest() {
        onView(withId(R.id.preferenceSalaryRangeSlider)).perform(adjustRangeSliderThumbs(0.0f, 1.0f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        Assert.assertEquals(min,sharedPreferences.getFloat(context.getString(R.string.salary_min), -1.0f),0);
    }
    @Test
    public void maxSalaryPreferenceTest() {
        onView(withId(R.id.preferenceSalaryRangeSlider)).perform(adjustRangeSliderThumbs(0.0f, 1.0f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        Assert.assertEquals(max,sharedPreferences.getFloat(context.getString(R.string.salary_max), -1.0f),0);
    }

    @Test
    public void lessThanMaxPayPreferenceTest() {
        onView(withId(R.id.preferenceSalaryRangeSlider)).perform(adjustRangeSliderThumbs(0.0f, 0.5f));
        onView(withId(R.id.confirmPreferencesButton)).perform(click());
        double midSalary = 60.0;
        Assert.assertEquals(midSalary,sharedPreferences.getFloat(context.getString(R.string.salary_max), -1.0f),0);
    }

}
