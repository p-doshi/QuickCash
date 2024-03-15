package dal.cs.quickcash3.slider;

import android.util.Range;

import androidx.annotation.NonNull;

import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class SalaryRangeSlider {
    private static final int[] PAY_RANGE_VALUES = {
          1,   2,   3,   4,   5,   6,   7,   8,   9,
         10,  20,  30,  40,  50,  60,  70,  80,  90,
        100, 200, 300, 400, 500, 600, 700, 800, 900, 1000
    };
    private static final int NUMBER_THUMBS = 2;
    private static final int DEFAULT_MIN_INDEX = 0;
    private static final int DEFAULT_MAX_INDEX = PAY_RANGE_VALUES.length - 1;

    private final RangeSlider rangeSlider;

    public SalaryRangeSlider(@NonNull RangeSlider rangeSlider) {
        this.rangeSlider = rangeSlider;

        rangeSlider.setValueFrom(0);
        rangeSlider.setValueTo(PAY_RANGE_VALUES.length - 1);
        rangeSlider.setStepSize(1.0f);

        List<Float> startingValues = new ArrayList<>(NUMBER_THUMBS);
        startingValues.add((float) DEFAULT_MIN_INDEX);
        startingValues.add((float) DEFAULT_MAX_INDEX);
        rangeSlider.setValues(startingValues);

        rangeSlider.setLabelFormatter(value -> "$" + mapValue(value));
    }

    private static int mapValue(float value) {
        int index = (int)Math.floor(value);
        return PAY_RANGE_VALUES[index];
    }

    public @NonNull Range<Double> getRange() {
        List<Float> values = rangeSlider.getValues();
        if (values.size() != NUMBER_THUMBS) {
            throw new IllegalArgumentException("Expected slider to have " + NUMBER_THUMBS + " thumbs");
        }

        double min = mapValue(values.get(0));
        double max = mapValue(values.get(0));
        if (min == PAY_RANGE_VALUES[0]) {
            min = 0.0;
        }
        if (max == PAY_RANGE_VALUES[PAY_RANGE_VALUES.length - 1]) {
            max = Double.POSITIVE_INFINITY;
        }
        return new Range<>(min, max);
    }
}
