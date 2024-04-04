package dal.cs.quickcash3.recycler;

import androidx.annotation.NonNull;

import dal.cs.quickcash3.data.Worker;

public interface WorkerViewHolder {
    /**
     * Set the worker on a view holder.
     *
     * @param worker The worker.
     */
    void setWorker(@NonNull Worker worker);
}
