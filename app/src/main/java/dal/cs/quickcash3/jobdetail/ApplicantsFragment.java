package dal.cs.quickcash3.jobdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Consumer;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.Worker;
import dal.cs.quickcash3.database.Database;

public class ApplicantsFragment extends Fragment {
    private final ApplicantManager manager;

    public ApplicantsFragment(@NonNull Database database, @NonNull AvailableJob job, @NonNull Consumer<Worker> acceptFunction) {
        super();
        manager = new ApplicantManager(database, job, acceptFunction);
    }

    @Override
    public @NonNull View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_applicants, container, false);

        RecyclerView applicantsRecycler = view.findViewById(R.id.applicantsRecyclerView);
        RecyclerView rejectedRecycler = view.findViewById(R.id.rejectedRecyclerView);
        manager.onCreate(applicantsRecycler, rejectedRecycler);

        return view;
    }

    @Override
    public void onDestroyView() {
        manager.onDestroy();
        super.onDestroyView();
    }
}
