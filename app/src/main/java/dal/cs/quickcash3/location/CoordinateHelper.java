package dal.cs.quickcash3.location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public final class CoordinateHelper {
    public static final double EARTH_RADIUS = 6378137; // In meters.

    // Utility class.
    private CoordinateHelper() {}

    @SuppressWarnings("PMD.LawOfDemeter") // This is the way it was meant to be done.
    public static @NonNull LatLngBounds getBoundingBox(@NonNull LatLng location, double radius) {
        double deltaLatitude = radius / EARTH_RADIUS;
        double deltaLongitude = radius / (EARTH_RADIUS * Math.cos(Math.PI * location.longitude / 180.0));

        double latMin = location.latitude - deltaLatitude * 180.0 / Math.PI;
        double lonMin = location.longitude - deltaLongitude * 180.0 / Math.PI;
        double latMax = location.latitude + deltaLatitude * 180.0 / Math.PI;
        double lonMax = location.longitude + deltaLongitude * 180.0 / Math.PI;

        return new LatLngBounds(new LatLng(latMin, lonMin), new LatLng(latMax, lonMax));
    }
}
