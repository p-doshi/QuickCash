package dal.cs.quickcash3.jobs;

import static dal.cs.quickcash3.database.DatabaseDirectory.AVAILABLE_JOBS;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.recycler.AvailableJobRecyclerViewAdapter;
import dal.cs.quickcash3.recycler.RecyclerItemClickListener;
import dal.cs.quickcash3.search.SearchFilter;
import dal.cs.quickcash3.util.AsyncLatch;

/**
 * A fragment representing a list of Items.
 */
public class JobListFragment extends Fragment {
    private static final String LOG_TAG = JobListFragment.class.getSimpleName();
    private final Database database;
    private final Context context;
    private final AsyncLatch<SearchFilter<AvailableJob>> asyncFilter;
    private final AvailableJobRecyclerViewAdapter adapter;
    private final Map<String,AvailableJob> availableJobMap = new HashMap<>();
    private final AtomicInteger callbackId = new AtomicInteger();

    public JobListFragment(
        @NonNull Context context,
        @NonNull Database database,
        @NonNull AsyncLatch<SearchFilter<AvailableJob>> asyncFilter,
        @NonNull Consumer<AvailableJob> displayCurrJob)
    {
        super();
        this.database = database;
        this.asyncFilter = asyncFilter;
        this.adapter = new AvailableJobRecyclerViewAdapter(displayCurrJob);
        this.context = context;
    }

    private void setFilterCallback() {
        asyncFilter.get(searchFilter -> {
            Log.d(LOG_TAG, "Restarting database search listener");
            database.removeListener(this.callbackId.get());
            adapter.reset();
            availableJobMap.clear();

            int callbackId = database.addSearchListener(AVAILABLE_JOBS.getValue(), AvailableJob.class, searchFilter,
                (key, job) -> {
                    Log.v(LOG_TAG, key + ": " + job);
                    if (job == null) {
                        availableJobMap.remove(key);
                    } else {
                        this.availableJobMap.put(key,job);
                        adapter.addJob(job);
                    }
                },
                error -> Log.w(LOG_TAG, "received database error: " + error));
            this.callbackId.set(callbackId);
        });
    }


    /**
     Searches through a list of available jobs based on the provided search filter and updates the adapter with the filtered results.
     @param filter The search filter to apply to the list of available jobs.
     It should implement the SearchFilter interface with the generic type of AvailableJob.
     The isValid method of the filter will be used to determine if a job should be included in the filtered results.
     @throws IllegalArgumentException if the filter parameter is null.
     */
    public void searchList(@NonNull SearchFilter<AvailableJob> filter){
        List<AvailableJob> newJobs = new ArrayList<>();

        for (AvailableJob job : availableJobMap.values()){
            if (filter.isValid(job)) {
                newJobs.add(job);
            }
        }

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
                new RecyclerItemClickListener(context, recyclerView ,adapter ));
        setFilterCallback();

        return view;
    }
}
