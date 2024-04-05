package dal.cs.quickcash3.worker;

import static dal.cs.quickcash3.database.DatabaseDirectory.COMPLETED_JOBS;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.search.SearchFilter;
import dal.cs.quickcash3.util.AsyncLatch;

public class WorkHistoryActivity extends AppCompatActivity {

    /*private double totalIncome;
    private double totalRating;
    RecyclerView jobHistoryList;
    TextView tvTotalIncome;
    TextView tvAverageReputation;
    private List<CompletedJob> jobHistories = new ArrayList<>();
    JobAdapter adapter;
    private  AsyncLatch<SearchFilter<CompletedJob>> asyncFilter;
    private  Database database;
    private final AtomicInteger callbackId = new AtomicInteger();
    private static final String LOG_TAG = CompletedJobListFragment.class.getSimpleName();
    private final Map<String, CompletedJob> completedJobMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_history);
        init();
        fetchHistories();
        adapter = new JobAdapter(jobHistories);
        jobHistoryList.setAdapter(adapter);
        jobHistoryList.setLayoutManager(new LinearLayoutManager(this));
        tvTotalIncome.setText(getString(R.string.total_income, String.format(Locale.US, "%.2f", totalIncome)));
        tvAverageReputation.setText(getString(R.string.average_reputation, String.format(Locale.US, "%.2f", totalRating / jobHistories.size())));
    }

    private void init() {
        jobHistoryList = findViewById(R.id.jobRecyclerView);
        tvTotalIncome = findViewById(R.id.totalIncome);
        tvAverageReputation = findViewById(R.id.averageReputation);
    }
    //TUDO
    private void fetchHistories(){
        asyncFilter.get(searchFilter -> {
            Log.d(LOG_TAG, "Restarting database search listener");
            database.removeListener(this.callbackId.get());
            completedJobMap.clear();

            int callbackId = database.addSearchListener(COMPLETED_JOBS.getValue(), CompletedJob.class, searchFilter, // 修改为COMPLETED_JOBS和CompletedJob
                    (key, job) -> {
                        Log.v(LOG_TAG, key + ": " + job);
                        if (job == null) {
                            completedJobMap.remove(key);
                        } else {
                            this.completedJobMap.put(key, job);
                            adapter.addJob(job);
                        }
                    },
                    error -> Log.w(LOG_TAG, "received database error: " + error));
            this.callbackId.set(callbackId);
        });
    }*/
}