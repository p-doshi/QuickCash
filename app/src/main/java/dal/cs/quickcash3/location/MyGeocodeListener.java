package dal.cs.quickcash3.location;

import android.content.Context;
import android.location.Address;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import dal.cs.quickcash3.util.AsyncReceiver;

class MyGeocodeListener<T> {
    private final Function<List<Address>, T> conversionFunction;
    private final AsyncReceiver<T> receiver;
    private final Looper mainLooper;
    private final boolean calledFromUiThread;

    public MyGeocodeListener(
        @NonNull Context context,
        @NonNull Function<List<Address>, T> conversionFunction,
        @NonNull Consumer<T> valueFunction,
        @NonNull Consumer<String> errorFunction)
    {
        this.conversionFunction = conversionFunction;
        receiver = new AsyncReceiver<>(valueFunction, errorFunction);
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

    private void receiveValue(@NonNull T location) {
        runOnCorrectThread(() -> receiver.receiveValue(location));
    }

    private void receiveError(@NonNull String error) {
        runOnCorrectThread(() -> receiver.receiveError(error));
    }

    public void onGeocode(@NonNull List<Address> addresses) {
        T value = conversionFunction.apply(addresses);
        receiveValue(value);
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
