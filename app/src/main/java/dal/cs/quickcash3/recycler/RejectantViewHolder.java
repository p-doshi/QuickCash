package dal.cs.quickcash3.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.function.Consumer;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.Worker;

public class RejectantViewHolder extends RecyclerView.ViewHolder implements WorkerViewHolder {
    private final TextView workerText;
    private final FloatingActionButton undoButton;
    private final Consumer<Worker> undoCallback;

    public RejectantViewHolder(@NonNull View itemView, @NonNull Consumer<Worker> undoCallback) {
        super(itemView);
        workerText = itemView.findViewById(R.id.worker);
        undoButton = itemView.findViewById(R.id.undoButton);
        this.undoCallback = undoCallback;
    }

    @Override
    public void setWorker(@NonNull Worker worker) {
        workerText.setText(worker.fullName());
        undoButton.setOnClickListener(view -> undoCallback.accept(worker));
    }
}
