package dal.cs.quickcash3.slider;

import androidx.annotation.NonNull;

import com.google.android.material.slider.RangeSlider;

public class MaxDistanceSlider extends MyRangeSlider {
    private static final int KILOMETER = 1000;
    private static final int[] MAX_DISTANCE_VALUES = {
        100, 200, 300, 500, KILOMETER,
        KILOMETER * 2, KILOMETER * 3, KILOMETER * 5, KILOMETER * 10,
        KILOMETER * 20, KILOMETER * 30, KILOMETER * 50, KILOMETER * 100,
        KILOMETER * 200, KILOMETER * 300
    };
    private static final int DEFAULT_INDEX = MAX_DISTANCE_VALUES.length - 1;

    public MaxDistanceSlider(@NonNull RangeSlider rangeSlider) {
        super(rangeSlider, MAX_DISTANCE_VALUES.length, (float) DEFAULT_INDEX);
    }

    @Override
    protected int mapValue(float value) {
        int index = (int)Math.floor(value);
        return MAX_DISTANCE_VALUES[index];
    }

    @Override
    protected @NonNull String formatLabel(float value) {
        int distance = mapValue(value);

        String label;
        if (distance % KILOMETER == 0) {
            int numKilometers = distance / KILOMETER;
            label = numKilometers + " km";
        }
        else {
            label = distance + " m";
        }
        return label;
    }

    public double getMaxDistance() {
        return getValues().get(0);
    }
}
