package dal.cs.quickcash3.recycler;

import android.annotation.SuppressLint;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;

public  class AvailableJobRecyclerViewAdapter extends RecyclerView.Adapter<AvailableJobRecyclerViewAdapter.ViewHolder> implements OnItemClickListener {
    private List<AvailableJob> jobs = new ArrayList<>();
    private final Consumer<AvailableJob> displayCurrJob;

    public AvailableJobRecyclerViewAdapter(@NonNull Consumer<AvailableJob> displayCurrJob){
        super();
        this.displayCurrJob = displayCurrJob;

    }
    public void addJob(@NonNull AvailableJob availableJob) {
        jobs.add(availableJob);
        notifyItemInserted(jobs.size() - 1);
    }

    public void reset() {
        notifyItemRangeRemoved(0, jobs.size());
        jobs.clear();
    }

    @SuppressLint("NotifyDataSetChanged") // TODO: fix this.
    public void newList(@NonNull List<AvailableJob> newJobs) {
        jobs = newJobs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.setJob(jobs.get(position));

    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    @Override
    public void onItemClick(@NonNull View view, int position) {
        displayCurrJob.accept(jobs.get(position));
    }

    @Override
    public void onLongItemClick(@NonNull View view, int position) {
        // Not used.
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView subheading;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subheading = itemView.findViewById(R.id.subhead);
        }

        public void setJob(@NonNull AvailableJob job) {

            title.setText(job.getTitle());
            subheading.setText(job.getDescription());
        }
    }
}
