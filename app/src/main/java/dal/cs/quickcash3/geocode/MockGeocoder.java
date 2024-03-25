package dal.cs.quickcash3.geocode;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class MockGeocoder implements MyGeocoder {
    private final List<Pair<Pattern, LatLng>> addressMatchers = new ArrayList<>();
    private final List<Pair<LatLngBounds, String>> locationMatchers = new ArrayList<>();

    /**
     * Adds the address regex to the end of the list of address matchers to use when geocoding.
     *
     * @param addressPattern The address pattern for the location.
     * @param location The location for all addresses that match the pattern.
     */
    public void addAddressMatcher(@NonNull Pattern addressPattern, @NonNull LatLng location) {
        addressMatchers.add(new Pair<>(addressPattern, location));
    }

    /**
     * Adds the location bounds to the end of the list of location matchers to use when geocoding.
     *
     * @param locationBounds The address pattern for the location.
     * @param address The location for all addresses that match the pattern.
     */
    public void addLocationMatcher(@NonNull LatLngBounds locationBounds, @NonNull String address) {
        locationMatchers.add(new Pair<>(locationBounds, address));
    }

    @Override
    public void fetchLocationFromAddress(
        @NonNull String address,
        @NonNull Consumer<LatLng> locationFunction,
        @NonNull Consumer<String> errorFunction)
    {
        for (Pair<Pattern, LatLng> pair : addressMatchers) {
            if (pair.first.matcher(address).matches()) {
                locationFunction.accept(pair.second);
                return;
            }
        }
        errorFunction.accept("No matching location: '" + address + '\'');
    }

    @Override
    public void fetchAddressFromLocation(
        @NonNull LatLng location,
        @NonNull Consumer<String> addressFunction,
        @NonNull Consumer<String> errorFunction)
    {
        for (Pair<LatLngBounds, String> pair : locationMatchers) {
            if (pair.first.contains(location)) {
                addressFunction.accept(pair.second);
                return;
            }
        }
        errorFunction.accept("No matching address: '" + location + '\'');
    }
}
