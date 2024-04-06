package dal.cs.quickcash3.worker;

import static dal.cs.quickcash3.database.DatabaseDirectory.COMPLETED_JOBS;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.search.SearchFilter;
import dal.cs.quickcash3.util.AsyncLatch;

public class WorkHistoryActivity extends AppCompatActivity {

    private double totalIncome;
    private double totalRating;
    RecyclerView jobHistoryList;
    TextView tvTotalIncome;
    TextView tvAverageReputation;
    private final List<CompletedJob> jobList = new ArrayList<>();
    JobAdapter adapter;
    private DatabaseReference mDatabase;

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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("public").child("completed_jobs");
        fetchJobsFromDatabase();
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
        jobList.add(job);
        // TODO: handle new jobs here.
    }

    private void removeWorker(@NonNull CompletedJob job) {
        jobList.remove(job);
        // TODO: handle deleted jobs here
    }

    private void updateUI(){
        tvTotalIncome.setText(getString(R.string.total_income, String.format(Locale.US, "%.2f", totalIncome)));
        tvAverageReputation.setText(getString(R.string.average_reputation,
                jobList.size() > 0 ? String.format(Locale.US, "%.2f", totalRating / jobList.size()) : "0"));
    }
}