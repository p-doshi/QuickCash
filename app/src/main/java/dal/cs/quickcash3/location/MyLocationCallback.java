package dal.cs.quickcash3.location;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import java.util.List;
import java.util.function.Consumer;

public class MyLocationCallback extends LocationCallback {
    private final FusedLocationProviderClient locationProviderClient;
    private final Consumer<Location> locationFunction;

    public MyLocationCallback(
        @NonNull FusedLocationProviderClient locationProviderClient,
        @NonNull Consumer<Location> locationFunction)
    {
        super();
        this.locationProviderClient = locationProviderClient;
        this.locationFunction = locationFunction;
    }

    @Override
    public void onLocationResult(@NonNull LocationResult locationResult) {
        List<Location> locations = locationResult.getLocations();
        if (locations.isEmpty()) {
            return;
        }

        Location location = locations.get(locations.size() - 1);
        if (location == null) {
            return;
        }

        locationFunction.accept(location);
        locationProviderClient.flushLocations();
    }
}
