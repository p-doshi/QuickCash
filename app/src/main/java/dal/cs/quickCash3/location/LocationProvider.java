package dal.cs.quickCash3.location;

import android.location.Location;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public interface LocationProvider {
    /**
     * Get the location. If successful, the locationFunction will be called. Otherwise, the
     * errorFunction will be called.
     * @param locationFunction The function that receives the location iff accessible. The location will never be null.
     * @param errorFunction The function that receives errors iff they occur.
     */
    void fetchLocation(@NonNull Consumer<Location> locationFunction, @NonNull Consumer<String> errorFunction);
}
