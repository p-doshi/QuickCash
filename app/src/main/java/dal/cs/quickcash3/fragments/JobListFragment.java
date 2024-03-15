package dal.cs.quickcash3.fragments;

import static dal.cs.quickcash3.database.DatabaseDirectory.AVAILABLE_JOBS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.search.SearchFilter;

/**
 * A fragment representing a list of Items.
 */
public class JobListFragment extends Fragment {
    private final Database database;
    private final Runnable showSearchPageFunction;
    private SearchFilter<AvailableJob> searchFilter;
    private int listenerId;

    public JobListFragment(@NonNull Database database, @NonNull Runnable showSearchPageFunction) {
        this.database = database;
        this.showSearchPageFunction = showSearchPageFunction;
    }

    public void setSearchFilter(@NonNull SearchFilter<AvailableJob> searchFilter) {
        this.searchFilter = searchFilter;
    }

    private void switchToSearchPage() {
        database.removeListener(listenerId);
        showSearchPageFunction.run();
    }

    @Override
    public @NonNull View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);
        listenerId = database.addSearchListener(AVAILABLE_JOBS.getValue(), AvailableJob.class, searchFilter,
            (key, job) -> {
                // TODO: write code here.
            },
            error -> {
                // TODO: write code here. Remove the line below as well.
                switchToSearchPage();
            });
        return view;
    }
}