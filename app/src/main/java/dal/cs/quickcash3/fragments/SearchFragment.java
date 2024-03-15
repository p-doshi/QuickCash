package dal.cs.quickcash3.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.function.Consumer;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.location.LocationProvider;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.search.SearchFilter;

public class SearchFragment extends Fragment {
    private final Consumer<SearchFilter<AvailableJob>> showResultsFunction;
    private final SearchFilter<AvailableJob> combinedFilter;

    public SearchFragment(
        @NonNull LocationProvider locationProvider,
        @NonNull Consumer<SearchFilter<AvailableJob>> showResultsFunction)
    {
        this.showResultsFunction = showResultsFunction;

        RegexSearchFilter<AvailableJob> uselessFilter = new RegexSearchFilter<>("title");
        uselessFilter.setPattern(Pattern.compile(".*"));
        combinedFilter = uselessFilter;
    }

    @Override
    public @Nullable View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        View searchView = inflater.inflate(R.layout.fragment_search, container, false);

        Button searchButton = searchView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> showResultsFunction.accept(combinedFilter));

        return searchView;
    }
}
