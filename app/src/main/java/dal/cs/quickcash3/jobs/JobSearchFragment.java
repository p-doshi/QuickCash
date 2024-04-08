package dal.cs.quickcash3.jobs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.function.Consumer;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.location.LocationProvider;

public class JobSearchFragment extends Fragment  {
    private FloatingActionButton filterButton;
    private final JobListFragment<AvailableJob> jobListFragment;
    private final SearchFilterFragment searchFragment;

    public JobSearchFragment(
        @NonNull Activity activity,
        @NonNull Database database,
        @NonNull LocationProvider locationProvider,
        @NonNull Consumer<AvailableJob> displayCurrJob)
    {
        super();
        this.searchFragment = new SearchFilterFragment(activity, locationProvider, this::showList);
        this.jobListFragment = new JobListFragment<>(
            activity, database, AvailableJob.DIR, AvailableJob.class, searchFragment.getFilter(), displayCurrJob);
    }

    @Override
    public @NonNull View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.jobs_search_page, container, false);
        this.setUpSearchBar(view);
        this.setUpfilterButton(view);
        replaceFragment(jobListFragment);
        return view;
    }

    public void setUpSearchBar(@NonNull View currentView) {
        SearchView searchView = currentView.findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new JobQueryTextListener(jobListFragment));
    }

    public void setUpfilterButton(@NonNull View currentView) {
        filterButton = currentView.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> {
            replaceFragment(searchFragment);
            filterButton.setVisibility(View.INVISIBLE);
        });
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.searchResultContainer, fragment);
        transaction.commit();
    }

    private void showList() {
        replaceFragment(jobListFragment);
        filterButton.setVisibility(View.VISIBLE);
    }
}
