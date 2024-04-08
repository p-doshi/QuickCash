package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicReference;

public final class GsonManager {
    private static final AtomicReference<Gson> GSON = new AtomicReference<>();

    // Utility class.
    private GsonManager() {}

    /**
     * Get a Gson instance.
     *
     * @return The Gson instance.
     */
    public static @NonNull Gson create() {
        if (GSON.get() == null) {
            GSON.set(new Gson());
        }
        return GSON.get();
    }
}
