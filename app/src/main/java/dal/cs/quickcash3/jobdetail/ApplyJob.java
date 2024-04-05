package dal.cs.quickcash3.jobdetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.Worker;


public class ApplyJob extends Fragment {

    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private AvailableJob currentJob;
    public ApplyJob(@NonNull AvailableJob currentJob,@NonNull Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference("available_jobs");
        this.currentJob = currentJob;
    }


    @Override
    public void onViewCreated(@NonNull View view,@NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = view.findViewById(R.id.applyForJob);

        button.setOnClickListener(v -> {
            applyForJob();
        });
    }

    private void applyForJob() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            String jobId = "your_job_id";
            Worker worker = new Worker();
            ApplicantManager applicantManager = new ApplicantManager(database,currentJob,);


            Toast.makeText(getContext(), "Applied for job successfully", Toast.LENGTH_SHORT).show();
        }
    }
}