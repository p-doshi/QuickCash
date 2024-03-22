package dal.cs.quickcash3.location;

import android.content.Context;
import android.location.Address;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.function.Consumer;

class MyGeocodeListener extends LocationReceiver {
    private final Looper mainLooper;
    private final boolean calledFromUiThread;

    public MyGeocodeListener(
        @NonNull Context context,
        @NonNull Consumer<LatLng> locationFunction,
        @NonNull Consumer<String> errorFunction)
    {
        super(locationFunction, errorFunction);
        mainLooper = context.getMainLooper();
        calledFromUiThread = mainLooper.isCurrentThread();
    }

    private void runOnCorrectThread(@NonNull Runnable function) {
        final boolean isUiThread = mainLooper.isCurrentThread();
        if (calledFromUiThread != isUiThread) {
            new Handler(mainLooper).post(function);
        }
        else {
            function.run();
        }
    }

    @Override
    public void receiveLocation(@NonNull LatLng location) {
        runOnCorrectThread(() -> super.receiveLocation(location));
    }

    @Override
    public void receiveError(@NonNull String error) {
        runOnCorrectThread(() -> super.receiveError(error));
    }

    public void onGeocode(@Nullable List<Address> addresses) {
        final LatLng location;

        if (addresses != null && !addresses.isEmpty()) {
            Address address1 = addresses.get(0);
            location = new LatLng(address1.getLatitude(), address1.getLongitude());
            receiveLocation(location);
        } else {
            receiveError("Could not get the location from the geocoder");
        }
    }

    public void onError(@Nullable String errorMessage) {
        if (errorMessage == null) {
            receiveError("Geocoder error with null message");
        }
        else {
            receiveError(errorMessage);
        }
    }
}
