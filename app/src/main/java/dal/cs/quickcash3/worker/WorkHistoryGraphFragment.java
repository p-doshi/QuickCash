package dal.cs.quickcash3.worker;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.ObjectSearchAdapter;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.search.SearchFilter;
import dal.cs.quickcash3.util.CustomObserver;

public class WorkHistoryGraphFragment extends Fragment {
    private final List<CompletedJob> jobList = new ArrayList<>();
    private final List<BarEntry> barEntries = new ArrayList<>();
    private Database database;
    private SearchFilter<CompletedJob> searchFilter;
    private BarChart barChart;
    private int callbackId;

    public WorkHistoryGraphFragment() {
        super();
    }
    public WorkHistoryGraphFragment(@NonNull Database database, @NonNull SearchFilter<CompletedJob> searchFilter){
        super();
        this.database = database;
        this.searchFilter = searchFilter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_history, container, false);
        barChart = view.findViewById(R.id.barChart);
        configureYAxis();
        fetchJobsFromDatabase();
        return view;
    }

    private void fetchJobsFromDatabase() {
        ObjectSearchAdapter<CompletedJob> searchAdapter = new ObjectSearchAdapter<>(searchFilter);
        searchAdapter.addObserver(new CustomObserver<>(this::addJob, this::removeWorker));
        callbackId = database.addDirectoryListener(CompletedJob.DIR, CompletedJob.class, searchAdapter::receive,
                error -> Log.w(TAG, "Received database error: " + error));
    }

    private void addJob(@NonNull CompletedJob job) {
        jobList.add(job);
        updateChartData();
    }

    private void removeWorker(@NonNull CompletedJob job) {
        jobList.remove(job);
        updateChartData();
    }

    private void updateChartData() {
        jobList.clear();
        barEntries.clear();
        List<String> xLabels = generateBarEntriesAndLabels();
        BarDataSet dataSet = new BarDataSet(barEntries, "Completed Jobs");
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        configureXAxis(xLabels);
        barChart.invalidate();
    }

    private List<String> generateBarEntriesAndLabels() {
        List<String> xLabels = new ArrayList<>();
        for (int i = 0; i < jobList.size(); i++) {
            CompletedJob job = jobList.get(i);
            float yValue = (float) job.getSalary();
            xLabels.add(formatDateForLabel(job.getCompletionDate()));
            barEntries.add(new BarEntry(i, yValue));
        }
        return xLabels;
    }

    private void configureXAxis(List<String> labels) {
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
    }

    private void configureYAxis() {
        barChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            private final DecimalFormat decimalFormat = new DecimalFormat("$###,###,###,##0.00");

            @Override
            public String getFormattedValue(float value) {
                return decimalFormat.format(value);
            }
        });
    }

    private String formatDateForLabel(String rawDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd", Locale.ENGLISH);
        try {
            Date date = inputFormat.parse(rawDate);
            if (date != null) {
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            Log.e(TAG, "Date parsing failed", e);
        }
        return "";
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "Stopping database search listener");
        database.removeListener(callbackId);
        super.onDestroyView();
    }
}