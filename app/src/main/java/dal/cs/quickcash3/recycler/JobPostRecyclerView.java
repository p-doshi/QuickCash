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
import dal.cs.quickcash3.data.JobPost;

public  class JobPostRecyclerView<T extends JobPost> extends RecyclerView.Adapter<JobPostRecyclerView.ViewHolder<T>> implements OnItemClickListener {
    private List<T> jobs = new ArrayList<>();
    private final Consumer<T> displayCurrJob;

    public JobPostRecyclerView(@NonNull Consumer<T> displayCurrJob){
        super();
        this.displayCurrJob = displayCurrJob;

    }

    public void addJob(@NonNull T availableJob) {
        jobs.add(availableJob);
        notifyItemInserted(jobs.size() - 1);
    }

    public void removeJob(@NonNull T availableJob) {
        int index = jobs.indexOf(availableJob);
        jobs.remove(index);
        notifyItemRemoved(index);
    }

    public void reset() {
        notifyItemRangeRemoved(0, jobs.size());
        jobs.clear();
    }


    public void newList(@NonNull List<T> newJobs) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ListDiffCallback<>(jobs, newJobs));
        jobs = newJobs;
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new ViewHolder<>(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int position) {
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

    public static class ViewHolder<T extends JobPost> extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView subheading;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subheading = itemView.findViewById(R.id.subhead);
        }

        public void setJob(@NonNull T job) {

            title.setText(job.getTitle());
            subheading.setText(job.getDescription());
        }
    }
}
