package dal.cs.quickcash3.geocode;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.function.Consumer;

public interface MyGeocoder {
    /**
     * Converts a postal address to coordinates.
     *
     * @param address the postal address to convert
     * @param locationFunction the consumer function for the location
     * @param errorFunction the consumer function for errors
     */
    void fetchLocationFromAddress(
        @NonNull String address,
        @NonNull Consumer<LatLng> locationFunction,
        @NonNull Consumer<String> errorFunction);

    /**
     * Converts coordinates to an address.
     *
     * @param location the coordinates to convert
     * @param addressFunction the consumer function for the address
     * @param errorFunction the consumer function for errors
     */
    void fetchAddressFromLocation(
        @NonNull LatLng location,
        @NonNull Consumer<String> addressFunction,
        @NonNull Consumer<String> errorFunction);
}
