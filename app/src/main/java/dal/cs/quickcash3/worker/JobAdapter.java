package dal.cs.quickcash3.worker;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.CompletedJob;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private final List<CompletedJob> jobList;

    public JobAdapter(List<CompletedJob> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        CompletedJob currentJob = jobList.get(position);
        holder.jobName.setText(currentJob.getTitle());
        holder.jobIncome.setText(String.format("Income: %.2f", currentJob.getSalary()));
        holder.jobRating.setText(String.format("Reputation: %.1f: NA"));

        final int ID_BASE = 10000;
        holder.jobName.setId(ID_BASE + position * 3);
        holder.jobIncome.setId(ID_BASE + 1 + position * 3);
        holder.jobRating.setId(ID_BASE + 2 + position * 3);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        public TextView jobName, jobIncome, jobRating;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.jobName);
            jobIncome = itemView.findViewById(R.id.jobIncome);
            jobRating = itemView.findViewById(R.id.jobRating);
        }
    }
}
