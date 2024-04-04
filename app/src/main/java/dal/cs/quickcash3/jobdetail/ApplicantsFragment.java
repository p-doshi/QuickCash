package dal.cs.quickcash3.jobdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;

public class ApplicantsFragment extends Fragment {
    @SuppressWarnings("PMD.UnusedPrivateField") // I will use this soon.
    private final AvailableJob job;

    public ApplicantsFragment(@NonNull AvailableJob job) {
        super();
        this.job = job;
    }

    @Override
    public @NonNull View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_applicants, container, false);
    }
}
