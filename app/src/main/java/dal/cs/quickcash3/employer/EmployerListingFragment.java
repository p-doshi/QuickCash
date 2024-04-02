package dal.cs.quickcash3.employer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.jobs.JobListFragment;
import dal.cs.quickcash3.search.SearchFilter;
import dal.cs.quickcash3.util.AsyncLatch;

public class EmployerListingFragment extends Fragment {
    private final JobListFragment jobListFragment;
    private final Runnable showJobPostForm;

    public EmployerListingFragment(
        @NonNull Context context,
        @NonNull Database database,
        @NonNull SearchFilter<AvailableJob> searchFilter,
        @NonNull Runnable showJobPostForm,
        @NonNull Consumer<AvailableJob> showJobDetails)
    {
        super();
        this.jobListFragment = new JobListFragment(context, database, new AsyncLatch<>(searchFilter), showJobDetails);
        this.showJobPostForm = showJobPostForm;
    }

    @Override
    public @NonNull View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_employer_listing, container, false);
        replaceFragment(jobListFragment);
        view.findViewById(R.id.addJobButton).setOnClickListener(unused -> showJobPostForm.run());
        return view;
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.jobListContainer, fragment);
        transaction.commit();
    }
}
