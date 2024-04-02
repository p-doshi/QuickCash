package dal.cs.quickcash3.jobdetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;

public class ApplicantsFragment extends Fragment {
    private final ApplicantsManager manager;

    public ApplicantsFragment(@NonNull Database database, @NonNull String jobId) {
        manager = new ApplicantsManager(database, jobId);
    }

    @Override
    public @NonNull View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicants, container, false);

        return view;
    }
}