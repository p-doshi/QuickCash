package dal.cs.quickcash3.location;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.function.Consumer;

class LocationReceiver {
    private final Consumer<LatLng> locationFunction;
    private final Consumer<String> errorFunction;

    public LocationReceiver(@NonNull Consumer<LatLng> locationFunction, @NonNull Consumer<String> errorFunction) {
        this.locationFunction = locationFunction;
        this.errorFunction = errorFunction;
    }

    public void receiveLocation(@NonNull LatLng location) {
        locationFunction.accept(location);
    }

    public void receiveError(@NonNull String error) {
        errorFunction.accept(error);
    }
}
