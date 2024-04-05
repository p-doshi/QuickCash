package dal.cs.quickcash3.recycler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import java.util.List;

import dal.cs.quickcash3.data.AvailableJob;

public class AvailableJobDiffCallback extends DiffUtil.Callback {
    private final List<AvailableJob> oldJobs;
    private final List<AvailableJob> newJobs;

    public AvailableJobDiffCallback(@NonNull List<AvailableJob> oldJobs,@NonNull List<AvailableJob> newJobs) {
        super();
        this.oldJobs = oldJobs;
        this.newJobs = newJobs;
    }

    @Override
    public int getOldListSize() {
        return oldJobs.size();
    }

    @Override
    public int getNewListSize() {
        return newJobs.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        AvailableJob oldJob = oldJobs.get(oldItemPosition);
        AvailableJob newJob = newJobs.get(newItemPosition);
        return oldJob.toString().equals(newJob.toString());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        AvailableJob oldJob = oldJobs.get(oldItemPosition);
        AvailableJob newJob = newJobs.get(newItemPosition);
        return oldJob.equals(newJob);
    }
}

