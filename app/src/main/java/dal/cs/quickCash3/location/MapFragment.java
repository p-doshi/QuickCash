package dal.cs.quickCash3.location;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dal.cs.quickCash3.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private Location location;
    private Pair<Location, String>[] jobs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // This R.layout. will need to change when integrated with WorkerDashboard
        View view = inflater.inflate(R.layout.test_mapdashlayout, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.mapContainer, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Add current location as a marker
        LatLng userLocation = getCurrentLocationLatLng();
        if (userLocation == null) {
            Log.d("MapFragment", "Location Returned Null");
            return;
        }
        googleMap.addMarker(new MarkerOptions().position(userLocation).title("Me"));

        // Add all the jobs as markers
        for (Pair<Location, String> job : jobs) {
            LatLng jobLocation = new LatLng(job.first.getLatitude(), job.first.getLongitude());
            String jobName = job.second;
            googleMap.addMarker(new MarkerOptions().position(jobLocation).title(jobName));
        }

        // Navigate camera to current position
        float zoomLevel = 12.0f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, zoomLevel));
    }

    /**
     * Sets the current location of the map to focus on
     * @param location Location to assign to map
     */
    public void setCurrentLocation(Location location) {
        this.location = location;
    }

    /**
     * Receives a list of job pairs to be output onto the map
     * @param jobs Array of pairs in form of Location, String
     */
    public void setJobList(Pair<Location, String>[] jobs) {
        this.jobs = jobs;
    }

    private LatLng getCurrentLocationLatLng() {
        if (location == null) {
            // Returning null indicates some kind of error
            return null;
        }
        return new LatLng(location.getLatitude(),location.getLongitude());
    }
}
