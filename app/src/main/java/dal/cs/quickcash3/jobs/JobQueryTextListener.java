package dal.cs.quickcash3.jobs;

import android.widget.SearchView;

import androidx.annotation.NonNull;

import java.util.regex.Pattern;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.search.RegexSearchFilter;

class JobQueryTextListener implements SearchView.OnQueryTextListener {
    private final JobListFragment<AvailableJob> jobListFragment;

    public JobQueryTextListener(@NonNull JobListFragment<AvailableJob> jobListFragment) {
        this.jobListFragment = jobListFragment;
    }

    private void handleSearch(@NonNull String query) {
        String[] words = query.split("\\s+");

        StringBuilder patternBuilder = new StringBuilder();
        for (String word : words) {
            String escapedWord = Pattern.quote(word);
            patternBuilder.append(".*").append(escapedWord).append(".*|");
        }

        String patternString = patternBuilder.toString();
        if (patternString.endsWith("|")) {
            patternString = patternString.substring(0, patternString.length() - 1);
        }

        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

        RegexSearchFilter<AvailableJob> regexSearchFilter = new RegexSearchFilter<>(AvailableJob::getTitle);
        regexSearchFilter.setPattern(pattern);

        jobListFragment.searchList(regexSearchFilter);
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
