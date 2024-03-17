package dal.cs.quickcash3.location;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class MockLocationProvider implements LocationProvider {
    private final Map<Integer, LocationReceiver> locationReceivers = new TreeMap<>();
    private Location location;
    private int nextReceiverId;

    public void setLocation(@NonNull LatLng latlng) {
        if (location == null) {
            location = new Location("");
        }
        location.setLatitude(latlng.latitude);
        location.setLongitude(latlng.longitude);

        for (LocationReceiver receiver : locationReceivers.values()) {
            receiver.receiveLocation(location);
        }
    }

    @Override
    public @Nullable Location getLastLocation() {
        return location;
    }

    @Override
    public int addLocationCallback(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction) {
        LocationReceiver receiver = new LocationReceiver(locationFunction, errorFunction);
        int receiverId = nextReceiverId++;
        locationReceivers.put(receiverId, receiver);

        if (location == null) {
            errorFunction.accept("Mock location not set");
        }
        else {
            locationFunction.accept(location);
        }

        return receiverId;
    }

    @Override
    public void removeLocationCallback(int callbackId) {
        locationReceivers.remove(callbackId);
    }
}
