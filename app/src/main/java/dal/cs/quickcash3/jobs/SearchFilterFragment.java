package dal.cs.quickcash3.jobs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
import dal.cs.quickcash3.search.RangeSearchFilter;
import dal.cs.quickcash3.search.SearchFilter;
import dal.cs.quickcash3.slider.DurationRangeSlider;
import dal.cs.quickcash3.slider.MaxDistanceSlider;
import dal.cs.quickcash3.slider.SalaryRangeSlider;
import dal.cs.quickcash3.util.AsyncLatch;
import dal.cs.quickcash3.util.Range;

public class SearchFilterFragment extends Fragment {
    private static final String LOG_TAG = SearchFilterFragment.class.getSimpleName();
    private final LocationProvider locationProvider;
    private final Runnable showResultsFunction;
    private final LocationSearchFilter<AvailableJob> locationFilter;
    private final RangeSearchFilter<AvailableJob, Double> salaryRangeFilter;
    private final RangeSearchFilter<AvailableJob, Double> durationFilter;
    private final SalaryRangeSlider salarySlider;
    private final DurationRangeSlider durationRangeSlider;
    private final MaxDistanceSlider maxDistanceSlider;
    private final AsyncLatch<SearchFilter<AvailableJob>> asyncFilter = new AsyncLatch<>();

    public SearchFilterFragment(
        @NonNull Activity activity,
        @NonNull LocationProvider locationProvider,
        @NonNull Runnable showResultsFunction)
    {
        super();
        this.showResultsFunction = showResultsFunction;
        this.locationProvider = locationProvider;

        locationFilter = new LocationSearchFilter<>(AvailableJob::getLatitude, AvailableJob::getLongitude);
        salaryRangeFilter = new RangeSearchFilter<>(AvailableJob::getSalary);
        durationFilter = new RangeSearchFilter<>(AvailableJob::getDuration);

        salarySlider = new SalaryRangeSlider(activity);
        durationRangeSlider = new DurationRangeSlider(activity);
        maxDistanceSlider = new MaxDistanceSlider(activity);

        locationFilter.setMaxDistance(maxDistanceSlider.getMaxDistance());
        salaryRangeFilter.setRange(salarySlider.getRange());
        durationFilter.setRange(durationRangeSlider.getRange());

        // Location filter is the first filter to apply.
        locationFilter.addNext(salaryRangeFilter).addNext(durationFilter);

    }

    public void fetchLocationForFilter() {
        locationProvider.fetchLocation(
            location -> {
                locationFilter.setLocation(location);
                asyncFilter.set(locationFilter);
            },
            error -> {
                Log.w(LOG_TAG, error);
                // Skip the location filter if we can't get a location.
                asyncFilter.set(salaryRangeFilter);
            }
        );
    }

    public @NonNull AsyncLatch<SearchFilter<AvailableJob>> getFilter() {
        return asyncFilter;
    }

    @Override
    public @Nullable View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        View searchView = inflater.inflate(R.layout.fragment_search_filter, container, false);

        salarySlider.setRangeSlider(searchView.findViewById(R.id.salaryRangeSlider));
        durationRangeSlider.setRangeSlider(searchView.findViewById(R.id.durationRangeSlider));
        maxDistanceSlider.setRangeSlider(searchView.findViewById(R.id.maxDistanceSlider));

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
