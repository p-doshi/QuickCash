package dal.cs.quickcash3.recycler;

import androidx.annotation.NonNull;

import dal.cs.quickcash3.data.Worker;

public interface WorkerViewHolder {
    /**
     * Binds the worker to a view holder.
     *
     * @param worker The worker to bind.
     */
    void onBind(@NonNull Worker worker);
}
