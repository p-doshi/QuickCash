package dal.cs.quickcash3.jobs;

import android.os.Bundle;
import android.util.Log;
import dal.cs.quickcash3.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.location.LocationProvider;
import dal.cs.quickcash3.search.LocationSearchFilter;
import dal.cs.quickcash3.search.NumericRangeSearchFilter;
import dal.cs.quickcash3.search.SearchFilter;
import dal.cs.quickcash3.slider.MaxDistanceSlider;
import dal.cs.quickcash3.slider.SalaryRangeSlider;
import dal.cs.quickcash3.slider.DurationRangeSlider;

public class SearchFilterFragment extends Fragment {
    private static final String LOG_TAG = SearchFilterFragment.class.getSimpleName();
    private final Runnable showResultsFunction;
    private final SearchFilter<AvailableJob> combinedFilter;
    private final LocationSearchFilter<AvailableJob> locationFilter;
    private final NumericRangeSearchFilter<AvailableJob> salaryRangeFilter;
    private final NumericRangeSearchFilter<AvailableJob> durationFilter;

    public SearchFilterFragment(
        @NonNull LocationProvider locationProvider,
        @NonNull Runnable showResultsFunction)
    {
        super();
        this.showResultsFunction = showResultsFunction;

        locationFilter = new LocationSearchFilter<>("latitude", "longitude", locationProvider);
        salaryRangeFilter = new NumericRangeSearchFilter<>("salary");
        durationFilter = new NumericRangeSearchFilter<>("duration");

        locationFilter.addNext(salaryRangeFilter).addNext(durationFilter);
        combinedFilter = locationFilter;
    }

    public @NonNull SearchFilter<AvailableJob> getCombinedFilter() {
        return combinedFilter;
    }

    @Override
    public @Nullable View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        View searchView = inflater.inflate(R.layout.fragment_search_filter, container, false);

        SalaryRangeSlider salarySlider =
            new SalaryRangeSlider(searchView.findViewById(R.id.salaryRangeSlider));
        DurationRangeSlider durationRangeSlider =
            new DurationRangeSlider(searchView.findViewById(R.id.durationRangeSlider));
        MaxDistanceSlider maxDistanceSlider =
            new MaxDistanceSlider(searchView.findViewById(R.id.maxDistanceSlider));

        Button searchButton = searchView.findViewById(R.id.applyButton);
        searchButton.setOnClickListener(view -> {
            Range<Double> newPayRange = salarySlider.getRange();
            Log.d(LOG_TAG, "Salary Range: " + newPayRange);
            salaryRangeFilter.setRange(newPayRange);

            Range<Double> newTimeEstimate = durationRangeSlider.getRange();
            Log.d(LOG_TAG, "Duration Range: " + newTimeEstimate + " hours");
            durationFilter.setRange(newTimeEstimate);

            double newMaxDistance = maxDistanceSlider.getMaxDistance();
            Log.d(LOG_TAG, "Max Distance: " + newMaxDistance + " m");
            locationFilter.setMaxDistance(newMaxDistance);

            showResultsFunction.run();
        });

        return searchView;
    }
}
