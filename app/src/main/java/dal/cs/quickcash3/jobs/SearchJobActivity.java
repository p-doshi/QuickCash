package dal.cs.quickcash3.jobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.fragments.JobListFragment;
import dal.cs.quickcash3.fragments.SearchFragment;
import dal.cs.quickcash3.location.LocationProvider;

public class SearchJobActivity extends Fragment {
    private ImageView filterIcon ;
    private final JobListFragment jobListFragment;
    private final SearchFragment searchFragment;

    public SearchJobActivity(@NonNull Database database, @NonNull LocationProvider locationProvider){
        super();
        this.searchFragment = new SearchFragment(locationProvider, this::showList);
        this.jobListFragment=new JobListFragment(database,searchFragment.getCombinedFilter());
    }

    @Override
    public @NonNull View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.jobs_search_page, container, false);
        this.setUpSearchBar(view);
        this.setUpFilterIcon(view);
        replaceFragment(jobListFragment);
        return view;
    }

    public void setUpSearchBar(@NonNull View currentView){
        SearchView searchView = currentView.findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new JobQueryTextListener(jobListFragment));
    }

    public void setUpFilterIcon(@NonNull View currentView){
        filterIcon = currentView.findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(v -> {
            replaceFragment(searchFragment);
            filterIcon.setVisibility(View.INVISIBLE);
        });
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.searchResultContainer, fragment);
        transaction.commit();
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod") // This is used.
    private void showList(){
        replaceFragment(jobListFragment);
        filterIcon.setVisibility(View.VISIBLE);
    }
}
