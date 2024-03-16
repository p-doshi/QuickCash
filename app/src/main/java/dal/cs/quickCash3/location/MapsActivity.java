package dal.cs.quickCash3.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import dal.cs.quickCash3.R;

public class MapsActivity extends Fragment implements OnMapReadyCallback {
    GoogleMap map;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // TODO: This R.layout. will need to change when integrated with WorkerDashboard
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
        map = googleMap;

        // Add current location as a marker
        LatLng userLocation = getLatLong();
        map.addMarker(new MarkerOptions().position(userLocation).title("Me"));
        LatLng job1 = new LatLng(44.641718, -63.584126);
        map.addMarker(new MarkerOptions().position(job1).title("Job 1"));
        LatLng job2 = new LatLng(44.6398496, -63.601291);
        map.addMarker(new MarkerOptions().position(job2).title("Job 2"));

        // Navigate camera to current position
        float zoomLevel = 12.0f;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, zoomLevel));
    }

    protected LatLng getLatLong() {
        return new LatLng(44.6356, -63.5952);
    }
}
