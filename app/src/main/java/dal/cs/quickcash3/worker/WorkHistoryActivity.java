package dal.cs.quickcash3.worker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.ObjectSearchAdapter;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.util.CustomObserver;

public class WorkHistoryActivity extends AppCompatActivity {

    private double totalIncome;
    private double totalRating;
    RecyclerView jobHistoryList;
    TextView tvTotalIncome;
    TextView tvAverageReputation;
    private final List<CompletedJob> jobList = new ArrayList<>();
    JobAdapter adapter;
    private int callbackId;
    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_history);
        init();
        adapter = new JobAdapter(jobList);
        jobHistoryList.setAdapter(adapter);
        jobHistoryList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void init() {
        jobHistoryList = findViewById(R.id.jobRecyclerView);
        tvTotalIncome = findViewById(R.id.totalIncome);
        tvAverageReputation = findViewById(R.id.averageReputation);
        database = new MyFirebaseDatabase();
        fetchJobsFromDatabase();

        adapter = new JobAdapter(jobList);
        jobHistoryList.setAdapter(adapter);
        jobHistoryList.setLayoutManager(new LinearLayoutManager(this));
        updateUI();
    }

    private void fetchJobsFromDatabase() {
        String workerId = ".*"; // TODO: Make this an actual worker ID.

        RegexSearchFilter<CompletedJob> searchFilter = new RegexSearchFilter<>(CompletedJob::getWorker);
        searchFilter.setPattern(Pattern.compile(workerId));

        ObjectSearchAdapter<CompletedJob> searchAdapter = new ObjectSearchAdapter<>(searchFilter);
        searchAdapter.addObserver(new CustomObserver<>(this::addJob, this::removeWorker));

        callbackId = database.addDirectoryListener(CompletedJob.DIR, CompletedJob.class, searchAdapter::receive,
                error -> {
                    // TODO: handle errors. You can probably just log it and move on.
                });
    }

    private void addJob(@NonNull CompletedJob job) {
        runOnUiThread(() -> {
            jobList.add(job);
            adapter.notifyItemInserted(jobList.size() - 1);
            updateUI();
            // TODO:
        });
    }

    private void removeWorker(@NonNull CompletedJob job) {
        runOnUiThread(() -> {
            int index = jobList.indexOf(job);
            if (index != -1) {
                jobList.remove(index);
                adapter.notifyItemRemoved(index);
                updateUI();
                // TODO:
            }
        });
    }

    private void updateUI() {
        double totalIncome = jobList.stream().mapToDouble(CompletedJob::getSalary).sum();
        double averageReputation = 0;

        tvTotalIncome.setText(getString(R.string.total_income, String.format(Locale.US, "%.2f", totalIncome)));
        tvAverageReputation.setText(getString(R.string.average_reputation, String.format(Locale.US, "%.2f", averageReputation)));
    }
}