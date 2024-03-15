package dal.cs.quickcash3.jobs;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.jobs.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<AvailableJob> mValues;
    private AvailableJob job;
    public MyItemRecyclerViewAdapter(List<AvailableJob> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.job = mValues.get(position);
        holder.title.setText(this.job.getTitle());
        holder.subheading.setText(this.job.getStartDate());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView subheading;
        public AvailableJob job;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title); // Change to R.id.title
            subheading = itemView.findViewById(R.id.subhead); // Change to R.id.subhead
        }

    }
}
