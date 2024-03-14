package dal.cs.quickcash3.jobs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import dal.cs.quickcash3.R;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private List<> itemList;

    // Constructor and other methods

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        YourItem item = itemList.get(position);
        holder.textViewTitle.setText(item.getTitle());
        // Bind other data to views
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        // Other views

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            // Initialize other views
        }
    }
}

