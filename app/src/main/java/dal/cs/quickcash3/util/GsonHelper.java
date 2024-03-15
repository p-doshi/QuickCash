package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public final class GsonHelper {
    // Utility class.
    private GsonHelper() {}

    public static @NonNull JsonElement getAt(@NonNull JsonElement root, @NonNull List<String> keys) {
        JsonElement output = root;
        for (String key : keys) {
            JsonObject object = output.getAsJsonObject();
            output = object.get(key);
        }
        if (output == null) {
            throw new IllegalArgumentException("Could not find element at: " + keys);
        }
        return output;
    }
}
