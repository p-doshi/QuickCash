package dal.cs.quickcash3.database;

import static dal.cs.quickcash3.database.DatabaseHelper.splitLocationIntoKeys;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class MockDatabase implements Database {
    private static final String KEY_NOT_FOUND = "Key not found: ";
    private final Map<Integer, DatabaseListener<?>> listenerMap = new TreeMap<>();
    private final Map<String, Object> data = new MapType();
    private int nextListenerId;

    // Let's create a class to avoid generic type cast warnings.
    public static class MapType extends TreeMap<String, Object> {
        private static final long serialVersionUID = 1L;
    }

    private Object recursiveGet(Object obj, @NonNull List<String> keys, int index) {
        if (index >= keys.size()) {
            return obj;
        }

        String key = keys.get(index);

        if (!(obj instanceof MapType)) {
            throw new IllegalArgumentException(KEY_NOT_FOUND + key);
        }
        Map<String, Object> map = (MapType)obj;

        if (!map.containsKey(key)) {
            throw new IllegalArgumentException(KEY_NOT_FOUND + key);
        }

        Object nestedData = map.get(key);
        int nextIndex = index + 1;
        return recursiveGet(nestedData, keys, nextIndex);
    }

    private <T> void recursiveSet(Map<String, Object> map, @NonNull List<String> keys, int index, T value) {
        assert index < keys.size();

        // Get the next key we are looking for.
        String key = keys.get(index);

        // Is this the last key?
        int nextIndex = index + 1;
        if (nextIndex == keys.size()) {
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

        recursiveSet(nestedMap, keys, nextIndex, value);
    }

    private void recursiveFindAndTrack(@NonNull List<Map<String, Object>> directories, @NonNull List<String> keys, int index) {
        String key = keys.get(index);
        Map<String, Object> lastDirectory = directories.get(directories.size() - 1);
        if (!lastDirectory.containsKey(key)) {
            throw new IllegalArgumentException(KEY_NOT_FOUND + key);
        }

        // Is this the last key?
        int nextIndex = index + 1;
        if (nextIndex == keys.size()) {
            lastDirectory.remove(key);
            recursiveDelete(directories);
            return;
        }

        Object nestedData = lastDirectory.get(key);
        if (!(nestedData instanceof MapType)) {
            throw new IllegalArgumentException(KEY_NOT_FOUND + key);
        }

        Map<String, Object> nextDirectory = (MapType)nestedData;
        directories.add(nextDirectory);
        recursiveFindAndTrack(directories, keys, nextIndex);
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
                    Object callbackValue = recursiveGet(data, listener.getKeys(), 0);
                    listener.sendValue(callbackValue);
                }
                catch (ClassCastException | IllegalArgumentException exception) {
                    listener.sendError(exception.getMessage());
                }
            }
        }
    }

    @Override
    public <T> void write(@NonNull String location, T value, @NonNull Consumer<String> errorFunction) {
        write(location, value, () -> {}, errorFunction);
    }

    @Override
    public <T> void write(@NonNull String location, T value, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        List<String> keys = splitLocationIntoKeys(location);
        if (keys.isEmpty()) {
            errorFunction.accept("Must provide a location to write data");
            return;
        }

        try {
            recursiveSet(data, keys, 0, value);
            runListeners(keys);
            successFunction.run();
        }
        catch (IllegalArgumentException exception) {
            errorFunction.accept(exception.getMessage());
        }
    }

    @Override
    public <T> void read(@NonNull String location, @NonNull Class<T> type, @NonNull Consumer<T> readFunction, @NonNull Consumer<String> errorFunction) {
        try {
            List<String> keys = splitLocationIntoKeys(location);
            Object obj = recursiveGet(data, keys, 0);
            T value = type.cast(obj);
            readFunction.accept(value);
        }
        catch (ClassCastException | IllegalArgumentException exception) {
            errorFunction.accept(exception.getMessage());
        }
    }

    @Override
    public <T> int addListener(@NonNull String location, @NonNull Class<T> type, @NonNull Consumer<T> readFunction, @NonNull Consumer<String> errorFunction) {
        int callbackId = nextListenerId++;
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
        List<String> keys = splitLocationIntoKeys(location);
        if (keys.isEmpty()) {
            errorFunction.accept("Must provide a location to delete data");
            return;
        }

        try {
            List<Map<String, Object>> directories = new ArrayList<>();
            directories.add(data);
            recursiveFindAndTrack(directories, keys, 0);

            runListeners(keys);

            successFunction.run();
        }
        catch (IllegalArgumentException exception) {
            errorFunction.accept(exception.getMessage());
        }
    }
}
