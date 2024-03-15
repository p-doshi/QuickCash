package dal.cs.quickcash3.jobs;

import static dal.cs.quickcash3.database.DatabaseDirectory.AVAILABLE_JOBS;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.search.SearchFilter;

/**
 * A fragment representing a list of Items.
 */
public class JobListFragment extends Fragment {
    private static final String LOG_TAG = JobListFragment.class.getName();
    private final Database database;
    private final SearchFilter<AvailableJob> searchFilter;
    private final MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter();
    private final Map<String,AvailableJob> availableJobMap = new HashMap<>();
    private int listenerId;

    public JobListFragment(@NonNull Database database, @NonNull SearchFilter<AvailableJob> searchFilter) {
        super();
        this.database = database;
        this.searchFilter = searchFilter;
    }

    public void resetList(@NonNull SearchFilter<AvailableJob> filter){
        List<AvailableJob> newJobs = new ArrayList<>();

        for (AvailableJob job : availableJobMap.values()){
            if(filter.isValid(job)){
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
            throw new ClassCastException("JOb list fragment not a recycler view");
        }

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setAdapter(adapter);
        listenerId = database.addSearchListener(AVAILABLE_JOBS.getValue(), AvailableJob.class, searchFilter,
            (key, job) -> {
                Log.v(LOG_TAG, key + ": " + job);
                if (job == null) {
                    availableJobMap.remove(key);
                } else {
                    this.availableJobMap.put(key,job);
                    adapter.addJob(job);
                }
            },
            error -> {
                Log.w(LOG_TAG, "received database error: " + error);
            });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        database.removeListener(listenerId);
        Log.d(LOG_TAG, "Destroyed");
    }
}