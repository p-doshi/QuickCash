package dal.cs.quickcash3.jobdetail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.JobPost;
import dal.cs.quickcash3.geocode.MyGeocoder;

public class JobDetailsPage extends Fragment {
    private static final String LOG_TAG = JobDetailsPage.class.getSimpleName();
    private final JobPost currentJob;
    private final Fragment subfragment;
    private final MyGeocoder geocoder;
    public JobDetailsPage(@NonNull MyGeocoder geocoder, @NonNull JobPost currentJob, @Nullable Fragment subfragment) {
        super();
        this.currentJob = currentJob;
        this.subfragment = subfragment;
        this.geocoder=geocoder;
    }

    @Override
    public @NonNull View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_job_details_page, container, false);

        TextView jobTitle =rootView.findViewById(R.id.jobTitle);
        TextView jobPay = rootView.findViewById(R.id.jobPay);
        TextView jobAddress = rootView.findViewById(R.id.jobAddress);
        TextView jobDescription = rootView.findViewById(R.id.jobDescription);

        jobTitle.setText(currentJob.getTitle());
        jobPay.setText(String.format(Locale.CANADA,"$%,.2f", currentJob.getSalary()));
        jobDescription.setText(currentJob.getDescription());
        LatLng addressCoordinates = new LatLng(currentJob.getLatitude(),currentJob.getLongitude());
        jobAddress.setText(coordinatesFormat(addressCoordinates));
        geocoder.fetchAddressFromLocation(addressCoordinates, jobAddress::setText, error ->Log.w(LOG_TAG,"GeoCoder error : "+ error));

        // Add child fragment for the bottom half
        if (subfragment != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            assert subfragment != null;
            transaction.replace(R.id.bottomHalfContainer, subfragment);
            transaction.commit();
        }

        return rootView;
    }

    private String coordinatesFormat (LatLng latLng){
        char north = latLng.latitude < 0.0 ? 'S' : 'N';
        char west = latLng.longitude < 0.0 ? 'W' : 'E';
        return String.format(Locale.CANADA, "%,.4f° %c, ", Math.abs(latLng.latitude), north) +
                String.format(Locale.CANADA, "%,.4f° %c", Math.abs(latLng.longitude), west);
    }
}
