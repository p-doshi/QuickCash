package dal.cs.quickcash3.slider;

import androidx.annotation.NonNull;

import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class MaxDistanceSlider {
    private static final int KILOMETER = 1000;
    private static final int[] MAX_DISTANCE_VALUES = {
        50, 100, 200, 300, 500, KILOMETER,
        KILOMETER * 2, KILOMETER * 3, KILOMETER * 5, KILOMETER * 10,
        KILOMETER * 20, KILOMETER * 30, KILOMETER * 50, KILOMETER * 100,
        KILOMETER * 200, KILOMETER * 300
    };
    private static final int NUMBER_THUMBS = 1;
    private static final int DEFAULT_INDEX = 0;

    private final RangeSlider rangeSlider;

    public MaxDistanceSlider(@NonNull RangeSlider rangeSlider) {
        this.rangeSlider = rangeSlider;

        rangeSlider.setValueFrom(0);
        rangeSlider.setValueTo(MAX_DISTANCE_VALUES.length - 1);
        rangeSlider.setStepSize(1.0f);

        List<Float> startingValues = new ArrayList<>(NUMBER_THUMBS);
        startingValues.add((float) DEFAULT_INDEX);
        rangeSlider.setValues(startingValues);

        rangeSlider.setLabelFormatter(MaxDistanceSlider::formatLabel);
    }

    private static int mapValue(float value) {
        int index = (int)Math.floor(value);
        return MAX_DISTANCE_VALUES[index];
    }

    public static @NonNull String formatLabel(float value) {
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
        List<Float> values = rangeSlider.getValues();
        if (values.size() != NUMBER_THUMBS) {
            throw new IllegalArgumentException("Expected slider to have " + NUMBER_THUMBS + " thumbs");
        }

        return mapValue(values.get(0));
    }
}
