package dal.cs.quickcash3.slider;

import android.app.Activity;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public abstract class MyRangeSlider {
    private final Activity activity;
    private final List<Float> startingValues;
    private final int numSteps;
    private final int numThumbs;
    private RangeSlider rangeSlider;

    public MyRangeSlider(@NonNull Activity activity, @NonNull List<Float> startingValues, int numSteps) {
        this.activity = activity;
        this.startingValues = startingValues;
        this.numSteps = numSteps;
        numThumbs = startingValues.size();
    }

    public void setRangeSlider(@NonNull RangeSlider rangeSlider) {
        this.rangeSlider = rangeSlider;

        rangeSlider.setValueFrom(0);
        rangeSlider.setValueTo(numSteps - 1);
        rangeSlider.setStepSize(1.0f);

        rangeSlider.setValues(startingValues);

        rangeSlider.setLabelFormatter(this::formatLabel);

        rangeSlider.setLabelBehavior(LabelFormatter.LABEL_VISIBLE);
        new Handler(activity.getMainLooper()).postDelayed(
            () -> rangeSlider.setLabelBehavior(LabelFormatter.LABEL_WITHIN_BOUNDS),
            1000);
    }

    protected abstract @NonNull String formatLabel(float value);

    protected abstract int mapValue(float value);

    protected @NonNull List<Float> getValues() {
        List<Float> values;

        if (rangeSlider == null) {
            values = new ArrayList<>(startingValues);
        }
        else {
            values = rangeSlider.getValues();
            if (values.size() != numThumbs) {
                throw new IllegalArgumentException("Expected slider to have " + numThumbs + " thumbs");
            }
        }

        values.replaceAll(value -> (float)mapValue(value));
        return values;
    }
}
