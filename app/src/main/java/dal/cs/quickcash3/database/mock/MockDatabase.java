package dal.cs.quickcash3.database.mock;

import static dal.cs.quickcash3.util.StringHelper.SLASH;
import static dal.cs.quickcash3.util.StringHelper.splitString;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.search.SearchFilter;

public class MockDatabase implements Database {
    public static final String EMPTY_LOCATION = "Location cannot be empty.";
    public static final String KEY_NOT_FOUND = "Could not find the key: ";
    private final Map<Integer, MockDatabaseListener<?>> listenerMap = new TreeMap<>();
    private int nextListenerId;
    private final Map<String, Object> data = new MapType();

    // Let's create a class to avoid generic type cast warnings.
    public static class MapType extends TreeMap<String, Object> {
        private static final long serialVersionUID = 1L;
    }
    
    private Object get(@NonNull List<String> keys) {
        if (keys.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_LOCATION);
        }

        Object currentObj = data;
        
        for (String key : keys) {
            if (!(currentObj instanceof MapType)) {
                throw new IllegalArgumentException(KEY_NOT_FOUND + key);
            }
            
            Map<String, Object> currentMap = (MapType) currentObj;
            if (!currentMap.containsKey(key)) {
                throw new IllegalArgumentException(KEY_NOT_FOUND + key);
            }

            currentObj = currentMap.get(key);
        }
        return currentObj;
    }

    private <T> void set(@NonNull List<String> keys, @NonNull T value) {
        if (keys.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_LOCATION);
        }

        Map<String, Object> currentMap = data;
        
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (i == keys.size() - 1) {
                currentMap.put(key, value);
            }
            else {
                if (!currentMap.containsKey(key) || !(currentMap.get(key) instanceof MapType)) {
                    currentMap.put(key, new MapType());
                }
                currentMap = (MapType) currentMap.get(key);
                assert currentMap != null;
            }
        }
    }

    private @NonNull List<Map<String, Object>> getMapsToDelete(@NonNull List<String> keys) {
        if (keys.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_LOCATION);
        }

        Map<String, Object> currentMap = data;
        List<Map<String, Object>> directoryPath = new ArrayList<>();
        directoryPath.add(currentMap);

        for (int index = 0; index < keys.size(); index++) {
            String key = keys.get(index);
            if (!currentMap.containsKey(key)) {
                throw new IllegalArgumentException(KEY_NOT_FOUND + key);
            }
            if (index == keys.size() - 1) {
                currentMap.remove(key);
            } else {
                Object nextMap = currentMap.get(key);
                if (!(nextMap instanceof MapType)) {
                    throw new IllegalArgumentException(KEY_NOT_FOUND + key);
                }
                currentMap = (MapType) nextMap;
                directoryPath.add(currentMap);
            }
        }

        return directoryPath;
    }

    private void removeEmptyMaps(@NonNull List<Map<String, Object>> directoryPath) {
        for (int i = directoryPath.size() - 1; i > 0; i--) {
            Map<String, Object> dir = directoryPath.get(i);
            if (dir.isEmpty()) {
                Map<String, Object> parentDir = directoryPath.get(i - 1);
                parentDir.values().removeIf(value -> value.equals(dir));
            } else {
                break;
            }
        }
    }

    private void runListeners(@NonNull List<String> keys) {
        for (MockDatabaseListener<?> listener : listenerMap.values()) {
            if (listener.isLocation(keys)) {
                try {
                    Object value = get(listener.getKeys());
                    listener.sendValue("", value);
                }
                catch (ClassCastException | IllegalArgumentException exception) {
                    listener.sendError(Objects.requireNonNull(exception.getMessage()));
                }
            }
        }
    }

    @Override
    public <T> void write(
        @NonNull String location,
        @NonNull T value,
        @NonNull Consumer<String> errorFunction)
    {
        write(location, value, () -> {}, errorFunction);
    }

    @Override
    public <T> void write(
        @NonNull String location,
        @NonNull T value,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        try {
            List<String> keys = splitString(location, SLASH);
            set(keys, value);
            runListeners(keys);
            successFunction.run();
        }
        catch (IllegalStateException | IllegalArgumentException exception) {
            errorFunction.accept(exception.getMessage());
        }
    }

    @Override
    public <T> void read(
        @NonNull String location,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        try {
            List<String> keys = splitString(location, SLASH);
            Object object = get(keys);
            T value = type.cast(object);
            readFunction.accept(value);
        }
        catch (ClassCastException | IllegalArgumentException exception) {
            errorFunction.accept(exception.getMessage());
        }
    }

    @Override
    public <T> int addListener(
        @NonNull String location,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        int callbackId = nextListenerId++;
        MockDatabaseListener<T> callbacks =
            new MockDatabaseValueListener<>(location, type, readFunction, errorFunction);
        listenerMap.put(callbackId, callbacks);

        // Read the current data.
        read(location, type, readFunction, errorFunction);

        return callbackId;
    }

    @Override
    public <T> int addSearchListener(
        @NonNull String location,
        @NonNull Class<T> type,
        @NonNull SearchFilter<T> filter,
        @NonNull BiConsumer<String, T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        int callbackId = nextListenerId++;
        MockDatabaseSearchListener<T> callbacks =
            new MockDatabaseSearchListener<>(location, type, filter, readFunction, errorFunction);
        listenerMap.put(callbackId, callbacks);

        // Read the current data.
        // TODO: search(location, type, callbacks);

        return -1;
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
            List<String> keys = splitString(location, SLASH);
            List<Map<String, Object>> maps = getMapsToDelete(keys);
            removeEmptyMaps(maps);

            runListeners(keys);
            successFunction.run();
        }
        catch (IllegalArgumentException exception) {
            errorFunction.accept(exception.getMessage());
        }
    }
}
