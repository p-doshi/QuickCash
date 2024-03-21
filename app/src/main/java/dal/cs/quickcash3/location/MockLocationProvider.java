package dal.cs.quickcash3.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import dal.cs.quickcash3.util.Promise;

public class MockLocationProvider implements LocationProvider {
    private final Map<Integer, LocationReceiver> locationReceivers = new TreeMap<>();
    private final List<LocationReceiver> oneTimeReceivers = new ArrayList<>();
    private LatLng location;
    private int nextReceiverId;

    public void setLocation(@NonNull LatLng latlng) {
        location = latlng;

        for (LocationReceiver receiver : locationReceivers.values()) {
            receiver.receiveLocation(location);
        }

        for (LocationReceiver receiver : oneTimeReceivers) {
            receiver.receiveLocation(location);
        }
        oneTimeReceivers.clear();
    }

    public void setError(@NonNull String error) {
        for (LocationReceiver receiver : locationReceivers.values()) {
            receiver.receiveError(error);
        }

        for (LocationReceiver receiver : oneTimeReceivers) {
            receiver.receiveError(error);
        }
        oneTimeReceivers.clear();
    }

    @Override
    public void fetchLocation(@NonNull Consumer<LatLng> locationFunction, @NonNull Consumer<String> errorFunction) {
        if (location == null) {
            LocationReceiver receiver = new LocationReceiver(locationFunction, errorFunction);
            oneTimeReceivers.add(receiver);
        }
        else {
            locationFunction.accept(location);
        }
    }

    @Override
    public int addLocationCallback(@NonNull Consumer<LatLng> locationFunction, @NonNull Consumer<String> errorFunction) {
        LocationReceiver receiver = new LocationReceiver(locationFunction, errorFunction);
        int receiverId = nextReceiverId++;
        locationReceivers.put(receiverId, receiver);

        if (location != null) {
            locationFunction.accept(location);
        }

        return receiverId;
    }

    @Override
    public void removeLocationCallback(int callbackId) {
        locationReceivers.remove(callbackId);
    }
}
