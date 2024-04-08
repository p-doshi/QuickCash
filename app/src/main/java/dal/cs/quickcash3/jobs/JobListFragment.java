package dal.cs.quickcash3.jobs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.JobPost;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.ObjectSearchAdapter;
import dal.cs.quickcash3.recycler.JobPostRecyclerView;
import dal.cs.quickcash3.recycler.RecyclerItemClickListener;
import dal.cs.quickcash3.search.SearchFilter;
import dal.cs.quickcash3.util.AsyncLatch;
import dal.cs.quickcash3.util.CustomObserver;
import dal.cs.quickcash3.util.ListObserver;

/**
 * A fragment representing a list of Items.
 */
public class JobListFragment<T extends JobPost> extends Fragment {
    private static final String LOG_TAG = JobListFragment.class.getSimpleName();
    private final Context context;
    private final Database database;
    private final String directory;
    private final Class<T> type;
    private final AsyncLatch<SearchFilter<T>> asyncFilter;
    private final JobPostRecyclerView<T> adapter;
    private final List<T> searchResults = new ArrayList<>();
    private int callbackId;

    public JobListFragment(
        @NonNull Context context,
        @NonNull Database database,
        @NonNull String directory,
        @NonNull Class<T> type,
        @NonNull AsyncLatch<SearchFilter<T>> asyncFilter,
        @NonNull Consumer<T> displayCurrJob)
    {
        super();
        this.context = context;
        this.database = database;
        this.directory = directory;
        this.type = type;
        this.asyncFilter = asyncFilter;
        this.adapter = new JobPostRecyclerView<>(displayCurrJob);
    }

    private void setFilterCallback() {
        asyncFilter.get(searchFilter -> {
            Log.i(LOG_TAG, "Starting database search listener");
            adapter.reset();
            searchResults.clear();

            ObjectSearchAdapter<T> searchAdapter = new ObjectSearchAdapter<>(searchFilter);
            searchAdapter.addObserver(new CustomObserver<>(adapter::addJob, adapter::removeJob));
            searchAdapter.addObserver(new ListObserver<>(searchResults));

            callbackId = database.addDirectoryListener(directory, type,
                searchAdapter::receive,
                error -> Log.w(LOG_TAG, "Received database error: " + error));
        });
    }


    /**
     Searches through a list of available jobs based on the provided search filter and updates the adapter with the filtered results.
     @param filter The search filter to apply to the list of available jobs.
     It should implement the SearchFilter interface with the generic type of AvailableJob.
     The isValid method of the filter will be used to determine if a job should be included in the filtered results.
     @throws IllegalArgumentException if the filter parameter is null.
     */
    public void searchList(@NonNull SearchFilter<T> filter){
        List<T> newJobs = searchResults.stream().filter(filter::isValid).collect(Collectors.toList());
        adapter.newList(newJobs);
    }

    @Override
    public @NonNull View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);
        if (!(view instanceof RecyclerView)) {
            throw new ClassCastException("Job list fragment not a recycler view");
        }

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView, adapter));
        setFilterCallback();

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.i(LOG_TAG, "Stopping database search listener");
        database.removeListener(callbackId);
        super.onDestroyView();
    }
}
