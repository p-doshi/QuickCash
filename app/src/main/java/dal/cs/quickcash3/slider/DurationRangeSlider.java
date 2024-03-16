package dal.cs.quickcash3.slider;

import static dal.cs.quickcash3.util.StringHelper.getPluralEnding;

import android.util.Range;

import androidx.annotation.NonNull;

import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class DurationRangeSlider {
    private static final int DAY = 24;
    private static final int WEEK = DAY * 7;
    private static final int[] TIME_ESTIMATE_VALUES = {
        1, 2, 4, 6, 12,          // Hours
        DAY, DAY * 2, DAY * 4,   // Days
        WEEK, WEEK * 2, WEEK * 4 // Weeks
    };
    private static final int NUMBER_THUMBS = 2;
    private static final int DEFAULT_MIN_INDEX = 0;
    private static final int DEFAULT_MAX_INDEX = TIME_ESTIMATE_VALUES.length - 1;

    private final RangeSlider rangeSlider;

    public DurationRangeSlider(@NonNull RangeSlider rangeSlider) {
        this.rangeSlider = rangeSlider;

        rangeSlider.setValueFrom(0);
        rangeSlider.setValueTo(TIME_ESTIMATE_VALUES.length - 1);
        rangeSlider.setStepSize(1.0f);

        List<Float> startingValues = new ArrayList<>(NUMBER_THUMBS);
        startingValues.add((float) DEFAULT_MIN_INDEX);
        startingValues.add((float) DEFAULT_MAX_INDEX);
        rangeSlider.setValues(startingValues);

        rangeSlider.setLabelFormatter(DurationRangeSlider::formatLabel);
    }

    private static int mapValue(float value) {
        int index = (int)Math.floor(value);
        return TIME_ESTIMATE_VALUES[index];
    }

    public static @NonNull String formatLabel(float value) {
        int time = mapValue(value);

        String label;
        if (time % WEEK == 0) {
            int numWeeks = time / WEEK;
            label = numWeeks + " week" + getPluralEnding(numWeeks);
        }
        else if (time % DAY == 0) {
            int numDays = time / DAY;
            label = numDays + " day" + getPluralEnding(numDays);
        }
        else {
            label = time + " hour" + getPluralEnding(time);
        }
        return label;
    }

    public @NonNull Range<Double> getRange() {
        List<Float> values = rangeSlider.getValues();
        if (values.size() != NUMBER_THUMBS) {
            throw new IllegalArgumentException("Expected slider to have " + NUMBER_THUMBS + " thumbs");
        }

        double min = mapValue(values.get(0));
        double max = mapValue(values.get(1));
        if (min == TIME_ESTIMATE_VALUES[0]) {
            min = 0.0;
        }
        if (max == TIME_ESTIMATE_VALUES[TIME_ESTIMATE_VALUES.length - 1]) {
            max = Double.POSITIVE_INFINITY;
        }
        return new Range<>(min, max);
    }
}
