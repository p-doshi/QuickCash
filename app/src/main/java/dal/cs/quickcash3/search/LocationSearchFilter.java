package dal.cs.quickcash3.search;

import static dal.cs.quickcash3.util.GsonHelper.getAt;
import static dal.cs.quickcash3.util.StringHelper.SLASH;
import static dal.cs.quickcash3.util.StringHelper.splitString;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;

import java.util.List;

import dal.cs.quickcash3.jobs.SearchFilterFragment;
import dal.cs.quickcash3.location.LocationHelper;
import dal.cs.quickcash3.location.LocationProvider;

public class LocationSearchFilter<T> extends SearchFilter<T> {
    private static final String LOG_TAG = LocationSearchFilter.class.getSimpleName();
    private final List<String> latKeys;
    private final List<String> longKeys;
    private LatLng location;
    private double maxDistance;

    public LocationSearchFilter(@NonNull String latKey, @NonNull String longKey) {
        super();
        latKeys = splitString(latKey, SLASH);
        longKeys = splitString(longKey, SLASH);
    }

    public void setLocation(@NonNull LatLng location) {
        this.location = location;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean isCurrentValid(@NonNull JsonElement root) {
        if (location == null) {
            throw new NullPointerException("Please provide a location to use the location search filter");
        }

        double latitude = getAt(root, latKeys).getAsDouble();
        double longitude = getAt(root, longKeys).getAsDouble();

        double distance = LocationHelper.distanceBetween(location, new LatLng(latitude, longitude));

        return distance <= maxDistance;
    }
}
