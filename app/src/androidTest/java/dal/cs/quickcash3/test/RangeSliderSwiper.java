package dal.cs.quickcash3.test;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

import static java.lang.Math.round;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.google.android.material.slider.RangeSlider;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RangeSliderSwiper implements ViewAction {
    private final List<Float> values;

    public RangeSliderSwiper(Float... values) {
        this.values = Arrays.asList(values);
    }

    @Override
    public Matcher<View> getConstraints() {
        return allOf(isDisplayed(), isAssignableFrom(RangeSlider.class));
    }

    @Override
    public String getDescription() {
        StringBuilder description = new StringBuilder("Setting range slider to: [");
        boolean first = true;
        for (Float value : values) {
            if (!first) {
                description.append(", ");
            }
            first = false;
            description.append(value);
        }
        description.append(']');
        return description.toString();
    }

    private static float normalizedToStep(float norm, float min, float max, int steps) {
        float normalQuantized = round(norm * steps) / (float)steps;
        return normalQuantized * (max - min) + min;
    }

    @Override
    public void perform(UiController uiController, View view) {
        RangeSlider slider = (RangeSlider) view;

        List<Float> sliderValues = slider.getValues();
        if (values.size() != sliderValues.size()) {
            throw new IllegalArgumentException(
                "Slider has " + sliderValues.size() + " thumbs and " + values.size() + " values were provided");
        }

        float min = slider.getValueFrom();
        float max = slider.getValueTo();
        float step = slider.getStepSize();
        int steps = round((max - min) / step);
        assert (max - min) / step == steps;

        List<Float> mappedValues = new ArrayList<>(values.size());
        for (Float value : values) {
            mappedValues.add(normalizedToStep(value, min, max, steps));
        }
        slider.setValues(mappedValues);
    }

    public static ViewAction adjustRangeSliderThumbs(Float... values) {
        return new RangeSliderSwiper(values);
    }
}
