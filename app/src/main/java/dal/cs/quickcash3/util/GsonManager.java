package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public final class GsonManager {
    private static Gson GSON;

    // Utility class.
    private GsonManager() {}

    /**
     * Get a Gson instance.
     *
     * @return The Gson instance.
     */
    public static @NonNull Gson create() {
        if (GSON == null) {
            GSON = new Gson();
        }
        return GSON;
    }
}
