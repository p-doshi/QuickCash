package dal.cs.quickcash3.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import java.util.function.Consumer;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.data.JobPost;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.jobs.JobListFragment;
import dal.cs.quickcash3.search.SearchFilter;
import dal.cs.quickcash3.util.AsyncLatch;

public class HistoryFragment extends Fragment {
    private final AsyncLatch<SearchFilter<CompletedJob>> asyncLatch = new AsyncLatch<>();
    private final SearchFilter<CompletedJob> searchFilter;
    private final JobListFragment<CompletedJob> jobListFragment;

    public HistoryFragment(
        @NonNull Context context,
        @NonNull Database database,
        @NonNull SearchFilter<CompletedJob> searchFilter,
        @NonNull Consumer<CompletedJob> showJobDetails)
    {
        super();
        this.searchFilter = searchFilter;
        this.jobListFragment = new JobListFragment<>(
            context, database, CompletedJob.DIR, CompletedJob.class, asyncLatch, showJobDetails);
    }

    @Override
    public @NonNull View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        asyncLatch.set(searchFilter);
        replaceFragment(jobListFragment);
        return view;
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.historyContainer, fragment);
        transaction.commit();
    }
}
