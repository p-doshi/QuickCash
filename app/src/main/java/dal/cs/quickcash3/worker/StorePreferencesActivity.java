package dal.cs.quickcash3.worker;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import dal.cs.quickcash3.R;
import dal.cs.quickcash3.slider.DurationRangeSlider;
import dal.cs.quickcash3.slider.SalaryRangeSlider;
import dal.cs.quickcash3.util.Range;
import dal.cs.quickcash3.util.StoreToSharedPref;

public class StorePreferencesActivity extends AppCompatActivity {
    private static final String LOG_TAG = StorePreferencesActivity.class.getSimpleName();
    private final SalaryRangeSlider salarySlider = new SalaryRangeSlider(this);
    private final DurationRangeSlider durationRangeSlider = new DurationRangeSlider(this);
    private StoreToSharedPref storeToSharedPref;
    Button updatePreferencesButton;
    CheckBox salaryCheckBox;
    CheckBox durationCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_store_preferences);
        init();
        setListeners();
    }

    private void init() {
        salarySlider.setRangeSlider(findViewById(R.id.preferenceSalaryRangeSlider));
        durationRangeSlider.setRangeSlider(findViewById(R.id.preferenceDurationRangeSlider));
        updatePreferencesButton = findViewById(R.id.confirmPreferencesButton);
        salaryCheckBox = findViewById(R.id.salaryCheckBox);
        durationCheckBox = findViewById(R.id.durationCheckBox);
        storeToSharedPref = new StoreToSharedPref(this);
    }

    private void setListeners() {
        updatePreferencesButton.setOnClickListener(v -> {
            Log.d(LOG_TAG, "pressed update preferences button");
            Range<Double> salaryRange = salarySlider.getRange();
            Range<Double> timeRange = durationRangeSlider.getRange();
            if (!salaryCheckBox.isChecked()) {
                storeToSharedPref.storeSalaryPrefToSharedPref(salaryRange);
            } else if (salaryCheckBox.isChecked()) {
                storeToSharedPref.clearSalaryPref();
            }

            if (!durationCheckBox.isChecked()) {
                storeToSharedPref.storeTimePrefToSharedPref(timeRange);
            } else if (durationCheckBox.isChecked()) {
                storeToSharedPref.clearTimePref();
            }
        });
    }
}
