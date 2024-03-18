package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public final class GsonHelper {
    public static Gson GSON = new Gson();

    // Utility class.
    private GsonHelper() {}

    /**
     * Gets the element inside the root element indexed by the set of keys.
     * Throws if the element does not exist.
     *
     * @param root The element to search inside.
     * @param keys The path of search for inside of the root element.
     * @return The element nested by the set of keys.
     */
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
