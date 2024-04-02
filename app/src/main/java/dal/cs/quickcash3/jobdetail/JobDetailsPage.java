package dal.cs.quickcash3.jobdetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;


public class JobDetailsPage extends Fragment {
    private final AvailableJob currentJob;
    private final Fragment subfragment;

    public JobDetailsPage(@NonNull AvailableJob currentJob, @NonNull Fragment subfragment) {
        super();
        this.currentJob = currentJob;
        this.subfragment = subfragment;
    }

    @NonNull
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_job_details_page, container, false);

        TextView jobTitle =rootView.findViewById(R.id.jobTitle);
        TextView jobPay = rootView.findViewById(R.id.jobPay);
        TextView jobAddress = rootView.findViewById(R.id.jobAddress);
        TextView jobDescription = rootView.findViewById(R.id.jobDescription);

        jobTitle.setText(currentJob.getTitle());
        jobPay.setText(String.format(Locale.CANADA,"$%,.2f", currentJob.getSalary()));
        jobDescription.setText(currentJob.getDescription());
        LatLng addressCoordinates = new LatLng(currentJob.getLatitude(),currentJob.getLongitude());
        jobAddress.setText(addressCoordinates.toString());

        // Add child fragment for the bottom half
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.bottomHalfContainer, subfragment);
        transaction.commit();

        return rootView;
    }
}