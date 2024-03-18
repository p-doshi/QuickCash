package dal.cs.quickcash3.search;

import static dal.cs.quickcash3.util.GsonHelper.getAt;
import static dal.cs.quickcash3.util.StringHelper.SLASH;
import static dal.cs.quickcash3.util.StringHelper.splitString;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;

import java.util.List;

import dal.cs.quickcash3.location.LocationHelper;
import dal.cs.quickcash3.location.LocationProvider;

public class LocationSearchFilter<T> extends SearchFilter<T> {
    private final List<String> latKeys;
    private final List<String> longKeys;
    private final LocationProvider locationProvider;
    private double maxDistance;

    public LocationSearchFilter(
        @NonNull String latKey,
        @NonNull String longKey,
        @NonNull LocationProvider locationProvider)
    {
        super();
        latKeys = splitString(latKey, SLASH);
        longKeys = splitString(longKey, SLASH);
        this.locationProvider = locationProvider;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean isCurrentValid(@NonNull JsonElement root) {
        LatLng currentLocation = locationProvider.getLastLocation();
        if (currentLocation == null) {
            throw new NullPointerException("Could not get location from location provider");
        }

        double latitude = getAt(root, latKeys).getAsDouble();
        double longitude = getAt(root, longKeys).getAsDouble();

        double distance = LocationHelper.distanceBetween(currentLocation, new LatLng(latitude, longitude));

        return distance <= maxDistance;
    }
}
