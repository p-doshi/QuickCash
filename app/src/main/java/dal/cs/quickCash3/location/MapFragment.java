package dal.cs.quickcash3.location;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;
import java.util.TreeMap;

import dal.cs.quickcash3.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private LatLng currentLocation;
    private final Map<String, LatLng> jobs = new TreeMap<>();

    @Override
    public @Nullable View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // This R.layout. will need to change when integrated with WorkerDashboard
        View view = inflater.inflate(R.layout.activity_google_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        return view;
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops") // No thank you.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Add current location as a marker
        if (currentLocation == null) {
            Log.d("MapFragment", "Location Returned Null");
            return;
        }
        googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Me"));

        // Add all the jobs as markers
        for (Map.Entry<String, LatLng> entry : jobs.entrySet()) {
            LatLng jobLocation = entry.getValue();
            String jobName = entry.getKey();
            googleMap.addMarker(new MarkerOptions().position(jobLocation).title(jobName));
        }

        // Navigate camera to current position
        float zoomLevel = 12.0f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));
    }

    /**
     * Sets the current currentLocation of the map to focus on
     * @param currentLocation Location to assign to map
     */
    public void setCurrentLocation(@NonNull LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * Adds a job to the list of jobs.
     *
     * @param jobTitle The title of the job.
     * @param location The location of the job.
     */
    public void addJob(@NonNull String jobTitle, @NonNull LatLng location) {
        jobs.put(jobTitle, location);
    }
}
