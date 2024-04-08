package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

public final class CopyHelper {
    public static final Gson GSON = new Gson();

    // Utility class.
    private CopyHelper() {}

    /**
     * Make a deep copy of a something.
     *
     * @param value The value to copy.
     * @return The deep copied value.
     */
    public static @NonNull Object deepClone(@NonNull Object value) {
        return GSON.fromJson(GSON.toJsonTree(value), value.getClass());
    }

    private static @NonNull Map<String, Method> methodsThatStartWith(@NonNull Object obj, @NonNull String prefix) {
        Map<String, Method> map = new TreeMap<>();
        for (Method method : obj.getClass().getMethods()) {
            String name = method.getName();
            if (name.startsWith(prefix)) {
                map.put(name.substring(prefix.length()), method);
            }
        }
        return map;
    }

    /**
     * Copy using all of the common public methods in source and destination.
     *
     * @param destination The destination to copy to.
     * @param source The source to copy from.
     */
    @SuppressWarnings("PMD.PreserveStackTrace") // I don't want to add the exceptions to this method.
    public static void copyTo(@NonNull Object destination, @NonNull Object source) {
        Map<String, Method> sourceGetters = methodsThatStartWith(source, "get");
        Map<String, Method> destinationSetters = methodsThatStartWith(destination, "set");

        try {
            for (Map.Entry<String, Method> entry : destinationSetters.entrySet()) {
                Method getter = sourceGetters.get(entry.getKey());
                if (getter != null) {
                    entry.getValue().invoke(destination, getter.invoke(source));
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Copy error: " + e.getMessage());
        }
    }
}
