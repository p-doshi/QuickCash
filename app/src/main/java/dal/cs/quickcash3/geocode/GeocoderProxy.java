package dal.cs.quickcash3.geocode;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** @noinspection RedundantSuppression*/
@SuppressWarnings({"unchecked", "deprecation"})
public class GeocoderProxy implements MyGeocoder {
    private static final int NUM_GEOCODE_TRIES = 3;
    private final Geocoder geocoder;

    public GeocoderProxy(@NonNull Context context) {
        this.geocoder = new Geocoder(context);
    }

    private static @NonNull List<Address> geocodeRunner(@NonNull Supplier<List<Address>> geocodeFunction) {
        String error = null;

        for (int i = 0; i < NUM_GEOCODE_TRIES; i++) {
            try {
                List<Address> addresses;
                do {
                    addresses = geocodeFunction.get();
                } while (addresses == null);
                return addresses;
            } catch (IllegalArgumentException e) {
                error = e.getMessage();
            }
        }

        throw new IllegalArgumentException(error);
    }

    private static @NonNull LatLng addressesToLocation(@NonNull List<Address> addresses) {
        if (addresses.isEmpty()) {
            throw new IllegalArgumentException("Could not get the location from the geocoder");
        }

        Address address = addresses.get(0);
        return new LatLng(address.getLatitude(), address.getLongitude());
    }

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

    @Override
    public void fetchLocationFromAddress(
        @NonNull String address,
        @NonNull Consumer<LatLng> locationFunction,
        @NonNull Consumer<String> errorFunction)
    {
        try {
            List<Address> addresses = geocodeRunner(() -> {
                try {
                    return geocoder.getFromLocationName(address, 1);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            });
            LatLng location = addressesToLocation(addresses);
            locationFunction.accept(location);
        }
        catch (IllegalArgumentException e) {
            errorFunction.accept(e.getMessage());
        }
    }

    @Override
    public void fetchAddressFromLocation(
        @NonNull LatLng location,
        @NonNull Consumer<String> addressFunction,
        @NonNull Consumer<String> errorFunction)
    {
        try {
            List<Address> addresses = geocodeRunner(() -> {
                try {
                    return geocoder.getFromLocation(location.latitude, location.longitude, 1);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            });
            String address = addressesToString(addresses);
            addressFunction.accept(address);
        }
        catch (IllegalArgumentException e) {
            errorFunction.accept(e.getMessage());
        }
    }
}
