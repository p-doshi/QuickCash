package dal.cs.quickcash3.search;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.function.Function;

import dal.cs.quickcash3.location.LocationHelper;

public class LocationSearchFilter<T> extends SearchFilter<T> {
    private final Function<T, Double> latFunction;
    private final Function<T, Double> longFunction;
    private LatLng location;
    private double maxDistance;

    public LocationSearchFilter(@NonNull Function<T, Double> latFunction, @NonNull Function<T, Double> longFunction) {
        super();
        this.latFunction = latFunction;
        this.longFunction = longFunction;
    }

    public void setLocation(@NonNull LatLng location) {
        this.location = location;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean isCurrentValid(@NonNull T elem) {
        if (location == null) {
            throw new NullPointerException("Please provide a location to use the location search filter");
        }

        double latitude = latFunction.apply(elem);
        double longitude = longFunction.apply(elem);

        double distance = LocationHelper.distanceBetween(location, new LatLng(latitude, longitude));

        return distance <= maxDistance;
    }
}
