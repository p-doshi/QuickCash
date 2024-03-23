package dal.cs.quickcash3.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import dal.cs.quickcash3.data.PostalAddress;

@SuppressWarnings("PMD.LawOfDemeter") // These are excessive for LatLng and Build.VERSION.
public final class LocationHelper {
    private static final Random RANDOM = new Random();
    private static final int NUM_GEOCODE_TRIES = 3;
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

    @SuppressWarnings("PMD.UnusedPrivateMethod") // This is used.
    private static @NonNull LatLng addressesToCoordinates(@NonNull List<Address> addresses) {
        if (addresses.isEmpty()) {
            throw new IllegalArgumentException("Could not get the location from the geocoder");
        }

        Address address = addresses.get(0);
        return new LatLng(address.getLatitude(), address.getLongitude());
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod") // This is used.
    private static @NonNull String addressesToString(@NonNull List<Address> addresses) {
        if (addresses.isEmpty()) {
            throw new IllegalArgumentException("Could not get the address from the geocoder");
        }

        Address address = addresses.get(0);
        String addressLine = address.getAddressLine(0);
        if (addressLine == null) {
            throw new IllegalArgumentException("Geocoder returned null address");
        }

        return addressLine;
    }

    private static <T> void geocodeRunner(
        @NonNull Supplier<List<Address>> geocodeFunction,
        @NonNull MyGeocodeListener<T> listener)
    {
        new Thread(() -> {
            String error = null;

            for (int i = 0; i < NUM_GEOCODE_TRIES; i++) {
                try {
                    List<Address> addresses;
                    do {
                        addresses = geocodeFunction.get();
                    } while (addresses == null);
                    listener.onGeocode(addresses);
                } catch (IllegalArgumentException e) {
                    error = e.getMessage();
                }
            }

            if (error != null) {
                listener.onError(error);
            }
        }).start();
    }

    public static void addressToCoordinates(
        @NonNull Context context,
        @NonNull PostalAddress address,
        @NonNull Consumer<LatLng> locationFunction,
        @NonNull Consumer<String> errorFunction)
    {
        Geocoder geocoder = new Geocoder(context);

        // Found a few sources saying to run this in a non-UI thread to avoid connection errors.
        MyGeocodeListener<LatLng> listener = new MyGeocodeListener<>(
            context,
            LocationHelper::addressesToCoordinates,
            locationFunction,
            errorFunction);

        geocodeRunner(
            () -> {
                try {
                    return geocoder.getFromLocationName(address.toString(), 1);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            },
            listener);
    }

    public static void coordinatesToAddress(
        @NonNull Context context,
        @NonNull LatLng location,
        @NonNull Consumer<String> addressFunction,
        @NonNull Consumer<String> errorFunction)
    {
        Geocoder geocoder = new Geocoder(context);

        // Found a few sources saying to run this in a non-UI thread to avoid connection errors.
        MyGeocodeListener<String> listener = new MyGeocodeListener<>(
            context,
            LocationHelper::addressesToString,
            addressFunction,
            errorFunction);

        geocodeRunner(
            () -> {
                try {
                    return geocoder.getFromLocation(location.latitude, location.longitude, 1);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            },
            listener);
    }
}
