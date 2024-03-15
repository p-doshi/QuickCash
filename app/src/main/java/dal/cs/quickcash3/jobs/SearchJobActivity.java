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

import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.fragments.JobListFragment;
import dal.cs.quickcash3.fragments.SearchFragment;
import dal.cs.quickcash3.location.LocationProvider;
import dal.cs.quickcash3.search.RegexSearchFilter;

public class SearchJobActivity extends Fragment {

//    private JobSearchHandler searchHandler;
    
    private ImageView filterIcon ;
    private JobListFragment jobListFragment;
    private SearchFragment searchFragment;
    public SearchJobActivity(Database database, LocationProvider locationProvider){

        this.searchFragment = new SearchFragment(locationProvider, this::showList);
        this.jobListFragment=new JobListFragment(database,searchFragment.getCombinedFilter());
    }

    private void showList(){
        replaceFragment(jobListFragment);
        filterIcon.setVisibility(View.VISIBLE);
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

    private void replaceFragment(@NonNull Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.searchResultContainer, fragment);
        transaction.commit();
    }
    
    public void setUpSearchBar(View currentView){
        SearchView searchView = currentView.findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleSearch(newText);
                return true;
            }
            });
    }

    public void setUpFilterIcon(View currentView){
        filterIcon = currentView.findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(v -> {
            replaceFragment(searchFragment);
            filterIcon.setVisibility(View.INVISIBLE);
        });
    }


    private void handleSearch(String query) {
        RegexSearchFilter<AvailableJob> regexSearchFilter = new RegexSearchFilter<>("title");
        regexSearchFilter.setPattern(Pattern.compile(".*"+query+".*", Pattern.CASE_INSENSITIVE));
        jobListFragment.resetList(regexSearchFilter);

    }
}
