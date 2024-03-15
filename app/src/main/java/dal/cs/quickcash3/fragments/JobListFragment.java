package dal.cs.quickcash3.fragments;

import static dal.cs.quickcash3.database.DatabaseDirectory.AVAILABLE_JOBS;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.jobs.MyItemRecyclerViewAdapter;
import dal.cs.quickcash3.search.SearchFilter;

/**
 * A fragment representing a list of Items.
 */
public class JobListFragment extends Fragment {
    private final Database database;

    private SearchFilter<AvailableJob> searchFilter;
    private int listenerId;
    private MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter();
    private Map<String,AvailableJob> availableJobMap = new HashMap<>();

    public JobListFragment(@NonNull Database database,SearchFilter<AvailableJob> searchFilter) {
        this.database = database;
        this.searchFilter = searchFilter;


    }

    public void resetList(SearchFilter<AvailableJob> filter){
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

                if (job==null){
                    availableJobMap.remove(key);
                }else {
                    this.availableJobMap.put(key,job);
                    adapter.addJob(job);
                }
            },
            error -> {
                // TODO: write code here. Remove the line below as well
            });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        database.removeListener(listenerId);
    }
}