package dal.cs.quickcash3.location;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class MockLocationProvider implements LocationProvider {
    private static final String LOG_TAG = MockLocationProvider.class.getName();
    private final AtomicReference<Map<Integer, LocationReceiver>> locationReceivers = new AtomicReference<>(new TreeMap<>());
    private final AtomicReference<Location> location = new AtomicReference<>(null);
    private Thread callbackThread;
    private int nextReceiverId;

    @SuppressWarnings("BusyWait") // There's nothing wrong with this.
    private void threadCallback() {
        try {
            while(!Thread.interrupted()) {
                Thread.sleep(1000);

                for (LocationReceiver receiver : locationReceivers.get().values()) {
                    receiver.receiveLocation(location.get());
                }
            }
        }
        catch (InterruptedException e) {
            Log.d(LOG_TAG, "Thread exiting");
        }
    }

    public void setLocation(@NonNull LatLng latlng) {
        Location loc = location.get();
        if (loc == null) {
            loc = new Location("");
        }
        loc.setLatitude(latlng.latitude);
        loc.setLongitude(latlng.longitude);
        location.set(loc);
    }

    @Override
    public @Nullable Location getLastLocation() {
        return location.get();
    }

    @Override
    public int addLocationCallback(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction) {
        LocationReceiver receiver = new LocationReceiver(locationFunction, errorFunction);
        int receiverId = nextReceiverId++;
        locationReceivers.get().put(receiverId, receiver);

        if (callbackThread == null || !callbackThread.isAlive()) {
            callbackThread = new Thread(this::threadCallback);
            callbackThread.start();
        }

        return receiverId;
    }

    @Override
    public void removeLocationCallback(int callbackId) {
        locationReceivers.get().remove(callbackId);
        if (locationReceivers.get().isEmpty() && callbackThread.isAlive()) {
            try {
                callbackThread.join();
            } catch (InterruptedException e) {
                Log.d(LOG_TAG, "Thread exiting");
            }
        }
    }
}
