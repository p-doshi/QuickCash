package dal.cs.quickcash3.database;

import static dal.cs.quickcash3.database.DatabaseHelper.splitLocationIntoKeys;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class MockDatabase implements Database {
    private final Map<Integer, DatabaseListener<?>> listenerMap = new TreeMap<>();
    private int lastCallbackId = 0;
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

    private <T> void recursiveSet(Map<String, Object> map, @NonNull List<String> keys, T value) {
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

    private void recursiveFindAndTrack(@NonNull List<Map<String, Object>> directories, @NonNull List<String> keys) {
        String key = keys.remove(0);
        Map<String, Object> lastDirectory = directories.get(directories.size() - 1);
        if (!lastDirectory.containsKey(key)) {
            throw new IllegalArgumentException("Key not found: " + key);
        }

        if (keys.isEmpty()) {
            lastDirectory.remove(key);
            recursiveDelete(directories);
            return;
        }

        Object nestedData = lastDirectory.get(key);
        if (!(nestedData instanceof MapType)) {
            throw new IllegalArgumentException("Key not found: " + key);
        }

        Map<String, Object> nextDirectory = (MapType)nestedData;
        directories.add(nextDirectory);
        recursiveFindAndTrack(directories, keys);
    }

    private void recursiveDelete(@NonNull List<Map<String, Object>> directories) {
        if (directories.isEmpty()) {
            return;
        }

        Map<String, Object> directory = directories.remove(directories.size() - 1);

        if (directory.size() > 1) {
            return;
        }

        directory.clear();
    }

    private void runListeners(@NonNull List<String> keys) {
        for (Map.Entry<Integer, DatabaseListener<?>> entry : listenerMap.entrySet()) {
            DatabaseListener<?> listener = entry.getValue();
            if (listener.isLocation(keys)) {
                try {
                    // Make a copy since the list will be modified.
                    Object callbackValue = recursiveGet(data, new ArrayList<>(listener.getKeys()));
                    listener.read(callbackValue);
                }
                catch (ClassCastException | IllegalArgumentException exception) {
                    listener.error(exception.getMessage());
                }
            }
        }
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

            // Make a copy since the list will be modified.
            recursiveSet(data, new ArrayList<>(keys), value);

            runListeners(keys);

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
        int callbackId = lastCallbackId++;
        DatabaseListener<T> callbacks = new DatabaseListener<>(location, type, readFunction, errorFunction);
        listenerMap.put(callbackId, callbacks);

        // Read the current data.
        read(location, type, readFunction, errorFunction);

        return callbackId;
    }

    @Override
    public void removeListener(int listenerId) {
        if (!listenerMap.containsKey(listenerId)) {
            throw new IllegalArgumentException("Could not find listener callback with ID: " + listenerId);
        }
        listenerMap.remove(listenerId);
    }

    @Override
    public void delete(@NonNull String location, @NonNull Consumer<String> errorFunction) {
        delete(location, () -> {}, errorFunction);
    }

    @Override
    public void delete(@NonNull String location, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        try {
            List<String> keys = splitLocationIntoKeys(location);
            if (keys.isEmpty()) {
                throw new IllegalArgumentException("Must provide a location to delete data");
            }

            List<Map<String, Object>> directories = new ArrayList<>();
            directories.add(data);

            // Make a copy since the list will be modified.
            recursiveFindAndTrack(directories, new ArrayList<>(keys));

            runListeners(keys);

            successFunction.run();
        }
        catch (IllegalArgumentException exception) {
            errorFunction.accept(exception.getMessage());
        }
    }
}
