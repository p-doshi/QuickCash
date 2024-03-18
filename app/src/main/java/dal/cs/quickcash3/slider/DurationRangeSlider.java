package dal.cs.quickcash3.slider;

import static dal.cs.quickcash3.util.StringHelper.getPluralEnding;

import androidx.annotation.NonNull;

import com.google.android.material.slider.RangeSlider;

import java.util.List;

import dal.cs.quickcash3.util.Range;

public class DurationRangeSlider extends MyRangeSlider {
    private static final int DAY = 24;
    private static final int WEEK = DAY * 7;
    private static final int[] DURATION_VALUES = {
        1, 2, 4, 6, 12,          // Hours
        DAY, DAY * 2, DAY * 4,   // Days
        WEEK, WEEK * 2, WEEK * 4 // Weeks
    };
    private static final int DEFAULT_MIN_INDEX = 0;
    private static final int DEFAULT_MAX_INDEX = DURATION_VALUES.length - 1;

    public DurationRangeSlider(@NonNull RangeSlider rangeSlider) {
        super(rangeSlider, DURATION_VALUES.length, (float) DEFAULT_MIN_INDEX, (float) DEFAULT_MAX_INDEX);
    }

    @Override
    protected int mapValue(float value) {
        int index = (int)Math.floor(value);
        return DURATION_VALUES[index];
    }

    @Override
    protected @NonNull String formatLabel(float value) {
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
        List<Float> values = getValues();
        double min = values.get(0);
        double max = values.get(1);

        if (min == DURATION_VALUES[0]) {
            min = 0.0;
        }
        if (max == DURATION_VALUES[DURATION_VALUES.length - 1]) {
            max = Double.POSITIVE_INFINITY;
        }

        return new Range<>(min, max);
    }
}
