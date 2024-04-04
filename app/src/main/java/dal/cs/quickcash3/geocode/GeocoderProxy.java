package dal.cs.quickcash3.geocode;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/** @noinspection RedundantSuppression*/
@SuppressWarnings({"unchecked", "deprecation"})
public class GeocoderProxy implements MyGeocoder {
    private static final String LOG_TAG = GeocoderProxy.class.getSimpleName();
    private static final int NUM_GEOCODE_TRIES = 3;
    private final Geocoder geocoder;
    private final Looper mainLooper;

    public GeocoderProxy(@NonNull Context context) {
        this.geocoder = new Geocoder(context);
        mainLooper = context.getMainLooper();
    }

    private <T> void geocodeRunner(
        @NonNull Supplier<List<Address>> geocodeFunction,
        @NonNull Function<List<Address>, T> conversionFunction,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        new Thread(() -> {
            T value = null;
            String error = null;
            for (int i = 0; i < NUM_GEOCODE_TRIES; i++) {
                try {
                    List<Address> addresses;
                    do {
                        Log.v(LOG_TAG, "Running geocoder function");
                        addresses = geocodeFunction.get();
                    } while (addresses == null);
                    Log.v(LOG_TAG, "Converting the geocoder results");
                    value = conversionFunction.apply(addresses);
                    break;
                } catch (IllegalArgumentException e) {
                    error = e.getMessage();
                    Log.v(LOG_TAG, "Geocoder error: " + error);
                }
            }

            if (value != null) {
                readFunction.accept(value);
            }
            else {
                errorFunction.accept(error);
            }
        }).start();
    }

    private <T> @NonNull Consumer<T> runOnSameThread(@NonNull Consumer<T> function) {
        if (mainLooper.isCurrentThread()) {
            return value -> new Handler(mainLooper).post(() -> function.accept(value));
        }
        else {
            return function;
        }
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

    @SuppressWarnings("PMD.ExceptionAsFlowControl") // That is not what this is in the slightest.
    @Override
    public void fetchLocationFromAddress(
        @NonNull String address,
        @NonNull Consumer<LatLng> locationFunction,
        @NonNull Consumer<String> errorFunction)
    {
        geocodeRunner(
            () -> {
                try {
                    return geocoder.getFromLocationName(address, 1);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            },
            GeocoderProxy::addressesToLocation,
            runOnSameThread(locationFunction),
            runOnSameThread(errorFunction));
    }

    @SuppressWarnings("PMD.ExceptionAsFlowControl") // That is not what this is in the slightest.
    @Override
    public void fetchAddressFromLocation(
        @NonNull LatLng location,
        @NonNull Consumer<String> addressFunction,
        @NonNull Consumer<String> errorFunction)
    {
        geocodeRunner(
            () -> {
                try {
                    return geocoder.getFromLocation(location.latitude, location.longitude, 1);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            },
            GeocoderProxy::addressesToString,
            runOnSameThread(addressFunction),
            runOnSameThread(errorFunction));
    }
}
