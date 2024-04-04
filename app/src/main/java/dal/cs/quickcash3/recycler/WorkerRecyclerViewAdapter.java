package dal.cs.quickcash3.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import dal.cs.quickcash3.data.Worker;

public class WorkerRecyclerViewAdapter<T extends RecyclerView.ViewHolder & WorkerViewHolder> extends RecyclerView.Adapter<T> {
    private final int resourceId;
    private final Function<View, T> viewConstructor;
    private final List<Worker> workers = new ArrayList<>();

    public WorkerRecyclerViewAdapter(@LayoutRes int resourceId, @NonNull Function<View, T> viewConstructor) {
        super();
        this.resourceId = resourceId;
        this.viewConstructor = viewConstructor;
    }

    public void addWorker(@NonNull Worker worker) {
        workers.add(worker);
        notifyItemInserted(workers.size() - 1);
    }

    public void removeWorker(@NonNull Worker worker) {
        if (!workers.contains(worker)) {
            throw new IllegalArgumentException("Worker not found: " + worker);
        }
        int index = workers.indexOf(worker);
        assert index != -1;
        notifyItemRemoved(index);
        workers.remove(index);
    }

    public void reset() {
        notifyItemRangeRemoved(0, workers.size());
        workers.clear();
    }

    @Override
    public @NonNull T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        return viewConstructor.apply(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final T holder, int position) {
        holder.setWorker(workers.get(position));
    }

    @Override
    public int getItemCount() {
        return workers.size();
    }
}
