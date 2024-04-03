package dal.cs.quickcash3.location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Random;

/** @noinspection RedundantSuppression*/
@SuppressWarnings({
    "unchecked", // I don't really know why this is necessary.
    "deprecation", // There is no other way to do geocoding.
    "PMD.LawOfDemeter" // These are excessive for LatLng and Build.VERSION.
})
public final class LocationHelper {
    private static final Random RANDOM = new Random();
    public static final double EARTH_RADIUS = 6378137; // In meters.

    // Utility class.
    private LocationHelper() {}

    private static double scaleNormalized(double normal, double min, double max) {
        return normal * (max - min) + min;
    }

    /**
     * Get a location bounding box for a given location and radius. The box will have side length
     * of roughly twice the radius. This function does not work very well near the poles.
     *
     * @param location The location to center the bounding box around.
     * @param radius The minimum distance from the location to any point on the bounding box.
     * @return The bounding box centered around the location with the given radius.
     */
    public static @NonNull LatLngBounds getBoundingBox(@NonNull LatLng location, double radius) {
        double deltaLatitude = radius / EARTH_RADIUS;
        double deltaLongitude = radius / (EARTH_RADIUS * Math.cos(Math.PI * location.longitude / 180.0));

        double latMin = location.latitude - deltaLatitude * 180.0 / Math.PI;
        double lonMin = location.longitude - deltaLongitude * 180.0 / Math.PI;
        double latMax = location.latitude + deltaLatitude * 180.0 / Math.PI;
        double lonMax = location.longitude + deltaLongitude * 180.0 / Math.PI;

        return new LatLngBounds(new LatLng(latMin, lonMin), new LatLng(latMax, lonMax));
    }

    /**
     * Get a random location from within a given area.
     *
     * @param area The area to pick a random location from.
     * @return A random location.
     */
    public static @NonNull LatLng randomLocation(@NonNull LatLngBounds area) {
        double lat = scaleNormalized(RANDOM.nextDouble(), area.southwest.latitude, area.northeast.latitude);
        double lng = scaleNormalized(RANDOM.nextDouble(), area.southwest.longitude, area.northeast.longitude);
        return new LatLng(lat, lng);
    }

    /**
     * Calculates the distance between two geographical locations using the
     * <a href="https://en.wikipedia.org/wiki/Haversine_formula">Haversine formula</a>.
     *
     * @param firstLocation The first location, not null.
     * @param secondLocation The second location, not null.
     * @return The distance between firstLocation and secondLocation in meters.
     */
    public static double distanceBetween(@NonNull LatLng firstLocation, @NonNull LatLng secondLocation) {
        double latitudeDifference = Math.toRadians(secondLocation.latitude - firstLocation.latitude);
        double longitudeDifference = Math.toRadians(secondLocation.longitude - firstLocation.longitude);

        double haversineFormulaNumerator = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference / 2)
            + Math.cos(Math.toRadians(firstLocation.latitude)) * Math.cos(Math.toRadians(secondLocation.latitude))
            * Math.sin(longitudeDifference / 2) * Math.sin(longitudeDifference / 2);

        double centralAngle = 2 * Math.atan2(Math.sqrt(haversineFormulaNumerator), Math.sqrt(1 - haversineFormulaNumerator));
        return EARTH_RADIUS * centralAngle;
    }
}
