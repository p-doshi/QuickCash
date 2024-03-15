package dal.cs.quickcash3.jobs;

import android.widget.SearchView;

import androidx.annotation.NonNull;

import java.util.regex.Pattern;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.search.RegexSearchFilter;

class JobQueryTextListener implements SearchView.OnQueryTextListener {
    private final JobListFragment jobListFragment;

    public JobQueryTextListener(@NonNull JobListFragment jobListFragment) {
        this.jobListFragment = jobListFragment;
    }

    private void handleSearch(@NonNull String query) {
        RegexSearchFilter<AvailableJob> regexSearchFilter = new RegexSearchFilter<>("title");
        regexSearchFilter.setPattern(Pattern.compile(".*"+query+".*", Pattern.CASE_INSENSITIVE));
        jobListFragment.resetList(regexSearchFilter);
    }

    @Override
    public boolean onQueryTextSubmit(@NonNull String query) {
        handleSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        handleSearch(newText);
        return true;
    }
}
