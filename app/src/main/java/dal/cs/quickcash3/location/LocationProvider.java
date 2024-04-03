package dal.cs.quickcash3.location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.errorprone.annotations.CheckReturnValue;

import java.util.function.Consumer;

public interface LocationProvider {
    /**
     * Set a callback to be updated with the current location every time it is updated. If any
     * errors occur, the errorFunction will be called.
     *
     * @param locationFunction The function that receives the location iff accessible. The location will never be null.
     * @param errorFunction The function that receives errors iff they occur.
     */
    @CheckReturnValue
    int addLocationCallback(@NonNull Consumer<LatLng> locationFunction, @NonNull Consumer<String> errorFunction);

    /**
     * Remove the callback with the matching callbackId.
     *
     * @param callbackId The ID of the callback to remove.
     */
    void removeLocationCallback(int callbackId);

    /**
     * Get the location. If successful, the locationFunction will be called. Otherwise, the
     * errorFunction will be called.
     *
     * @param locationFunction The function that receives the location if they are accessible. The location will never be null.
     * @param errorFunction The function that receives errors if they occur.
     */
    void fetchLocation(@NonNull Consumer<LatLng> locationFunction, @NonNull Consumer<String> errorFunction);
}
