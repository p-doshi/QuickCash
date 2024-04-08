package dal.cs.quickcash3.jobdetail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;


public class ApplyJob extends Fragment {

    private static final String LOG_TAG = ApplyJob.class.getSimpleName();

    private final Database database;
    private final AvailableJob currentJob;
    private final String userID;

    public ApplyJob(@NonNull Database database, @NonNull AvailableJob currentJob,@Nullable String userID){
        super();
        this.database=database;
        this.currentJob=currentJob;
        this.userID=userID;
    }

    @Override
    public @NonNull View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_apply_job, container, false);
        Button button = view.findViewById(R.id.applyForJob);
        button.setEnabled(false);
        if(userID != null) {
            if( !currentJob.isApplicant(userID)) {
                button.setEnabled(true);
                button.setOnClickListener(v -> applyForJob(view,button));
            }else{
                TextView statusLabel = view.findViewById(R.id.statusApplyJob);
                statusLabel.setText(R.string.appliedJob);
            }
        }else{
            TextView statusLabel = view.findViewById(R.id.statusApplyJob);
            statusLabel.setText(R.string.log_in);
        }

        return view;
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private void applyForJob(@NonNull View view, Button button) {

        assert userID != null;
        currentJob.addApplicant(userID);
        currentJob.writeToDatabase(database,error ->Log.d(LOG_TAG,"Database error : "+error));
        Toast.makeText(getContext(), getString(R.string.applySuccess),Toast.LENGTH_SHORT).show();
        TextView statusLabel = view.findViewById(R.id.statusApplyJob);
        statusLabel.setText(R.string.applySuccess);
        button.setEnabled(false);
    }
}