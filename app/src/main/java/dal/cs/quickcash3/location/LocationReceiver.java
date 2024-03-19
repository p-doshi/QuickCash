package dal.cs.quickcash3.location;

import android.location.Location;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

class LocationReceiver {
    private final Consumer<Location> locationFunction;
    private final Consumer<String> errorFunction;

    public LocationReceiver(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction) {
        this.locationFunction = locationFunction;
        this.errorFunction = errorFunction;
    }

    public void receiveLocation(@NonNull Location location) {
        locationFunction.accept(location);
    }

    public void receiveError(@NonNull String error) {
        errorFunction.accept(error);
    }
}
