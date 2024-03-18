package dal.cs.quickcash3.slider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.slider.RangeSlider;

import java.util.Arrays;
import java.util.List;

import dal.cs.quickcash3.util.Range;

public class SalaryRangeSlider extends MyRangeSlider {
    private static final int[] PAY_RANGE_VALUES = {
          1,   2,   3,   4,   5,   6,   7,   8,   9,
         10,  20,  30,  40,  50,  60,  70,  80,  90,
        100, 200, 300, 400, 500, 600, 700, 800, 900, 1000
    };
    private static final List<Float> DEFAULT_VALUES = Arrays.asList(
        0.0f,
        (float)PAY_RANGE_VALUES.length - 1);

    public SalaryRangeSlider() {
        super(DEFAULT_VALUES, PAY_RANGE_VALUES.length);
    }

    @Override
    protected int mapValue(float value) {
        int index = (int)Math.floor(value);
        return PAY_RANGE_VALUES[index];
    }

    @Override
    protected @NonNull String formatLabel(float value) {
        return "$" + mapValue(value);
    }

    public @NonNull Range<Double> getRange() {
        List<Float> values = getValues();
        double min = values.get(0);
        double max = values.get(1);

        if (min == PAY_RANGE_VALUES[0]) {
            min = 0.0;
        }
        if (max == PAY_RANGE_VALUES[PAY_RANGE_VALUES.length - 1]) {
            max = Double.POSITIVE_INFINITY;
        }

        return new Range<>(min, max);
    }
}
