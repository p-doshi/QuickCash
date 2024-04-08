package dal.cs.quickcash3.worker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.slider.DurationRangeSlider;
import dal.cs.quickcash3.slider.SalaryRangeSlider;
import dal.cs.quickcash3.util.Range;
import dal.cs.quickcash3.util.SharedPrefHelper;

public class StorePreferencesFragment extends Fragment {
    private static final String LOG_TAG = StorePreferencesActivity.class.getSimpleName();
    private final SalaryRangeSlider salarySlider;
    private final DurationRangeSlider durationRangeSlider;
    private final SharedPrefHelper sharedPrefHelper;
    Button updatePreferencesButton;
    CheckBox salaryCheckBox;
    CheckBox durationCheckBox;

    public StorePreferencesFragment(Activity activity) {
        salarySlider = new SalaryRangeSlider(activity);
        durationRangeSlider = new DurationRangeSlider(activity);
        sharedPrefHelper = new SharedPrefHelper(activity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View storePreferenceView = inflater.inflate(R.layout.fragment_store_preferences, container, false);
        super.onCreate(savedInstanceState);

        init(storePreferenceView);
        setListeners();

        return storePreferenceView;
    }

    private void init(View storePreferenceView) {
        salarySlider.setRangeSlider(storePreferenceView.findViewById(R.id.preferenceSalaryRangeSlider));
        durationRangeSlider.setRangeSlider(storePreferenceView.findViewById(R.id.preferenceDurationRangeSlider));
        updatePreferencesButton = storePreferenceView.findViewById(R.id.confirmPreferencesButton);
        salaryCheckBox = storePreferenceView.findViewById(R.id.salaryCheckBox);
        durationCheckBox = storePreferenceView.findViewById(R.id.durationCheckBox);
    }

    private void setListeners() {
        updatePreferencesButton.setOnClickListener(v -> {
            Log.d(LOG_TAG, "pressed update preferences button");
            Range<Double> salaryRange = salarySlider.getRange();
            Range<Double> timeRange = durationRangeSlider.getRange();
            if (!salaryCheckBox.isChecked()) {
                sharedPrefHelper.storeSalaryPrefToSharedPref(salaryRange);
            } else if (salaryCheckBox.isChecked()) {
                sharedPrefHelper.clearSalaryPref();
            }

            if (!durationCheckBox.isChecked()) {
                sharedPrefHelper.storeTimePrefToSharedPref(timeRange);
            } else if (durationCheckBox.isChecked()) {
                sharedPrefHelper.clearTimePref();
            }
        });
    }
}
