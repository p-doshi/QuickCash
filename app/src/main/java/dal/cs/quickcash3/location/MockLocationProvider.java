package dal.cs.quickcash3.location;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.function.Consumer;

public class MockLocationProvider implements LocationProvider {
    private Location location;

    public void setLocation(LatLng latlng) {
        if (location == null) {
            location = new Location("");
        }
        location.setLatitude(latlng.latitude);
        location.setLongitude(latlng.longitude);
    }

    @Override
    public void fetchLocation(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction) {
        if (location == null) {
            errorFunction.accept("Must set a location for the mock location provider");
        }
        else {
            locationFunction.accept(location);
        }
    }
}
