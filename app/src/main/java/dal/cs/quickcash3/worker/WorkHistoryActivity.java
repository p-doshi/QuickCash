package dal.cs.quickcash3.worker;

import android.os.Bundle;
import android.util.Log;
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
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.util.CustomObserver;

public class WorkHistoryActivity extends AppCompatActivity {
    private static final String TAG = WorkHistoryActivity.class.getSimpleName();
    RecyclerView jobHistoryList;
    private List<CompletedJob> jobList;
    JobAdapter adapter;
    private int callbackId;
    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_history);
        init();
        database = new MockDatabase();
        jobList = new ArrayList<>();
        fetchJobsFromDatabase();
        adapter = new JobAdapter(jobList);
        jobHistoryList.setAdapter(adapter);
        jobHistoryList.setLayoutManager(new LinearLayoutManager(this));
        if(jobList.isEmpty()){
            Log.e(TAG, "JobList is empty");
        }
    }

    private void init() {
        jobHistoryList = findViewById(R.id.jobRecyclerView);
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
        Log.e(TAG, "Data load complete.");
    }

    private void addJob(@NonNull CompletedJob job) {
        jobList.add(job);
    }

    private void removeWorker(@NonNull CompletedJob job) {
        jobList.remove(job);
    }
}