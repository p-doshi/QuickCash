package dal.cs.quickcash3.slider;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public abstract class MyRangeSlider {
    private final RangeSlider rangeSlider;
    private final int numThumbs;

    public MyRangeSlider(
        @NonNull RangeSlider rangeSlider,
        int numSteps,
        float... startingValues)
    {
        this.rangeSlider = rangeSlider;
        numThumbs = startingValues.length;

        rangeSlider.setValueFrom(0);
        rangeSlider.setValueTo(numSteps - 1);
        rangeSlider.setStepSize(1.0f);

        List<Float> values = new ArrayList<>(numThumbs);
        for (float value : startingValues) {
            values.add(value);
        }
        rangeSlider.setValues(values);

        rangeSlider.setLabelFormatter(this::formatLabel);

        rangeSlider.setLabelBehavior(LabelFormatter.LABEL_VISIBLE);
        new Handler().postDelayed(
            () -> rangeSlider.setLabelBehavior(LabelFormatter.LABEL_WITHIN_BOUNDS),
            1000);
    }

    protected abstract @NonNull String formatLabel(float value);

    protected abstract int mapValue(float value);

    protected @NonNull List<Float> getValues() {
        List<Float> values = rangeSlider.getValues();
        if (values.size() != numThumbs) {
            throw new IllegalArgumentException("Expected slider to have " + numThumbs + " thumbs");
        }

        values.replaceAll(value -> (float) mapValue(value));
        return values;
    }
}
