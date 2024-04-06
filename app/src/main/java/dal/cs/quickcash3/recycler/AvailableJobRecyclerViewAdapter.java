package dal.cs.quickcash3.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
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

    public void removeJob(@NonNull AvailableJob availableJob) {
        int index = jobs.indexOf(availableJob);
        jobs.remove(index);
        notifyItemRemoved(index);
    }

    public void reset() {
        notifyItemRangeRemoved(0, jobs.size());
        jobs.clear();
    }


    public void newList(@NonNull List<AvailableJob> newJobs) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new AvailableJobDiffCallback(jobs, newJobs));
        jobs = newJobs;
        diffResult.dispatchUpdatesTo(this);
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
        AvailableJob currJob = jobs.get(position);
        this.displayCurrJob.accept(currJob);
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
