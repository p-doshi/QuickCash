package dal.cs.quickcash3.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.function.Consumer;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.Worker;

public class ApplicantViewHolder extends RecyclerView.ViewHolder implements WorkerViewHolder {
    private final TextView workerText;
    private final FloatingActionButton acceptButton;
    private final FloatingActionButton rejectButton;
    private final Consumer<Worker> acceptCallback;
    private final Consumer<Worker> rejectCallback;

    public ApplicantViewHolder(
        @NonNull View itemView,
        @NonNull Consumer<Worker> acceptCallback,
        @NonNull Consumer<Worker> rejectCallback)
    {
        super(itemView);
        workerText = itemView.findViewById(R.id.worker);
        acceptButton = itemView.findViewById(R.id.acceptButton);
        rejectButton = itemView.findViewById(R.id.rejectButton);
        this.acceptCallback = acceptCallback;
        this.rejectCallback = rejectCallback;
    }

    @Override
    public void onBind(@NonNull Worker worker) {
        workerText.setText(worker.fullName());
        acceptButton.setOnClickListener(view -> acceptCallback.accept(worker));
        rejectButton.setOnClickListener(view -> rejectCallback.accept(worker));
    }
}
