package dal.cs.quickcash3.jobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.util.AsyncLatch;

public class JobListingsFragment extends Fragment {
    private final JobListFragment jobListFragment;

    public JobListingsFragment(@NonNull Database database) {
        super();
        RegexSearchFilter<AvailableJob> searchFilter = new RegexSearchFilter<>("title");
        searchFilter.setPattern(Pattern.compile(".*"));
        this.jobListFragment = new JobListFragment(database, new AsyncLatch<>(searchFilter));
    }

    @Override
    public @NonNull View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.job_listing_page, container, false);
        replaceFragment(jobListFragment);
        return view;
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.jobListContainer, fragment);
        transaction.commit();
    }
}
