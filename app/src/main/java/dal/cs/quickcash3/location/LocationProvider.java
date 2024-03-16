package dal.cs.quickcash3.location;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.function.Consumer;

public interface LocationProvider {
    /**
     * Set a callback to be updated with the current location every time it is updated. If any
     * errors occur, the errorFunction will be called.
     *
     * @param locationFunction The function that receives the location iff accessible. The location will never be null.
     * @param errorFunction The function that receives errors iff they occur.
     */
    int addLocationCallback(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction);

    /**
     * Remove the callback with the matching callbackId.
     *
     * @param callbackId The ID of the callback to remove.
     */
    void removeLocationCallback(int callbackId);

    /**
     * Get the last location immediately. There are no guarantees that this will return an accurate
     * or even non null location.
     * @return The last received location. Or null if there is none.
     */
    @Nullable Location getLastLocation();
}
