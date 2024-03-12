package dal.cs.quickCash3.database;

import static dal.cs.quickCash3.database.DatabaseHelper.splitLocationIntoKeys;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class MockDatabase implements Database {
    private final Map<String, Object> data = new MapType();

    // Let's create a class to avoid generic type cast warnings.
    public static class MapType extends TreeMap<String, Object> {
        private static final long serialVersionUID = 1L;
    }

    public MockDatabase() {}

    private Object recursiveGet(Object obj, @NonNull List<String> keys) {
        if (keys.isEmpty()) {
            return obj;
        }

        String key = keys.remove(0);

        if (!(obj instanceof MapType)) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        Map<String, Object> map = (MapType)obj;

        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Key not found: " + key);
        }

        Object nestedData = map.get(key);
        return recursiveGet(nestedData, keys);
    }

    private <T> void recursiveSet(@NonNull Map<String, Object> map, @NonNull List<String> keys, T value) {
        assert !keys.isEmpty();

        // Get the next key we are looking for.
        String key = keys.remove(0);

        // Is this the last key?
        if (keys.isEmpty()) {
            // TODO: serialize the type into a JSON object.
            map.put(key, value);
            return;
        }

        Map<String, Object> nestedMap;
        Object nestedData = map.get(key);
        if (nestedData instanceof MapType) {
            nestedMap = (MapType)nestedData;
        }
        else {
            nestedMap = new MapType();
            map.put(key, nestedMap);
        }

        recursiveSet(nestedMap, keys, value);
    }

    @Override
    public <T> void write(String location, T value, Consumer<String> errorFunction) {
        write(location, value, () -> {}, errorFunction);
    }

    @Override
    public <T> void write(String location, T value, Runnable successFunction, Consumer<String> errorFunction) {
        try {
            List<String> keys = splitLocationIntoKeys(location);
            if (keys.isEmpty()) {
                throw new IllegalArgumentException("Must provide a location to write data");
            }

            recursiveSet(data, keys, value);

            successFunction.run();
        }
        catch (IllegalArgumentException exception) {
            errorFunction.accept(exception.getMessage());
        }
    }

    @Override
    public <T> void read(String location, Class<T> type, Consumer<T> readFunction, Consumer<String> errorFunction) {
        try {
            List<String> keys = splitLocationIntoKeys(location);
            Object obj = recursiveGet(data, keys);
            T value = type.cast(obj);
            readFunction.accept(value);
        }
        catch (ClassCastException | IllegalArgumentException exception) {
            errorFunction.accept(exception.getMessage());
        }
    }

    @Override
    public <T> int addListener(@NonNull String location, @NonNull Class<T> type, @NonNull Consumer<T> readFunction, @NonNull Consumer<String> errorFunction) {
        // TODO
        return -1;
    }

    @Override
    public void removeListener(int listenerId) {
        // TODO
    }

    @Override
    public void delete(@NonNull String location, @NonNull Consumer<String> errorFunction) {
        delete(location, () -> {}, errorFunction);
    }

    @Override
    public void delete(@NonNull String location, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        // TODO
    }
}
