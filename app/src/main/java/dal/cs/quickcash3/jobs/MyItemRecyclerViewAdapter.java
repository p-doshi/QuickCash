package dal.cs.quickcash3.jobs;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AvailableJob}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    public List<AvailableJob> jobs = new ArrayList<>();


    public void addJob (AvailableJob availableJob){
        jobs.add(availableJob);
        notifyItemInserted(jobs.size()-1);
    }
    @SuppressLint("NotifyDataSetChanged") //will do later
    public void newList (List<AvailableJob> newJobs){
        jobs = newJobs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.job = jobs.get(position);
        holder.title.setText(holder.job.getTitle());
        holder.subheading.setText(holder.job.getDescription());
    }

    @Override
    public int getItemCount() {
        return jobs.size();
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
