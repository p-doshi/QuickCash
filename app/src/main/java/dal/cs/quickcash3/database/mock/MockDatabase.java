package dal.cs.quickcash3.database.mock;

import static dal.cs.quickcash3.util.CopyHelper.deepClone;
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

@SuppressWarnings("PMD.GodClass") // Shush! I am God.
public class MockDatabase implements Database {
    public static final String EMPTY_PATH = "Path cannot be empty.";
    public static final String KEY_NOT_FOUND = "Could not find the key: ";
    private final Map<Integer, MockDatabaseValueListener<?>> valueListenerMap = new TreeMap<>();
    private final Map<Integer, MockDatabaseSearchListener<?>> searchListenerMap = new TreeMap<>();
    private int nextListenerId;
    private final Map<String, Object> data = new MapType();

    // Let's create a class to avoid generic type cast warnings.
    protected static class MapType extends TreeMap<String, Object> {
        private static final long serialVersionUID = 1L;
    }

    private Object get(@NonNull List<String> keys) {
        if (keys.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_PATH);
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

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops") // There is no reason to make this recursive.
    private <T> void set(@NonNull List<String> keys, @NonNull T value) {
        if (keys.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_PATH);
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
            throw new IllegalArgumentException(EMPTY_PATH);
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

    private void runListener(@NonNull MockDatabaseValueListener<?> listener) {
        try {
            Object value = get(listener.getKeys());
            listener.sendValue(value);
        }
        catch (ClassCastException | IllegalArgumentException exception) {
            listener.sendError(Objects.requireNonNull(exception.getMessage()));
        }
    }

    private void runListener(@NonNull MockDatabaseSearchListener<?> listener) {
        try {
            Object object = get(listener.getKeys());
            Map<String, Object> map = (MapType) object;
            listener.update(map);
        }
        catch (ClassCastException | IllegalArgumentException exception) {
            listener.sendError(Objects.requireNonNull(exception.getMessage()));
        }
    }

    private void runListeners(@NonNull List<String> keys) {
        for (MockDatabaseValueListener<?> listener : valueListenerMap.values()) {
            if (listener.isPath(keys)) {
                runListener(listener);
            }
        }

        for (MockDatabaseSearchListener<?> listener : searchListenerMap.values()) {
            if (listener.isPath(keys)) {
                runListener(listener);
            }
        }
    }

    @Override
    public <T> void write(
        @NonNull String path,
        @NonNull T value,
        @NonNull Consumer<String> errorFunction)
    {
        write(path, value, () -> {}, errorFunction);
    }

    @Override
    public <T> void write(
        @NonNull String path,
        @NonNull T value,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        try {
            List<String> keys = splitString(path, SLASH);
            // Make a copy of the value.
            set(keys, deepClone(value));
            runListeners(keys);
            successFunction.run();
        }
        catch (ClassCastException | IllegalArgumentException exception) {
            errorFunction.accept(exception.getMessage());
        }
    }

    @Override
    public <T> void read(
        @NonNull String path,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        try {
            List<String> keys = splitString(path, SLASH);
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
        @NonNull String path,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        int callbackId = nextListenerId++;
        MockDatabaseValueListener<T> listener =
            new MockDatabaseValueListener<>(path, type, readFunction, errorFunction);
        valueListenerMap.put(callbackId, listener);

        runListener(listener);

        return callbackId;
    }

    @Override
    public <T> int addDirectoryListener(
        @NonNull String path,
        @NonNull Class<T> type,
        @NonNull BiConsumer<String, T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        int callbackId = nextListenerId++;
        MockDatabaseSearchListener<T> listener =
            new MockDatabaseSearchListener<>(path, type, readFunction, errorFunction);
        searchListenerMap.put(callbackId, listener);

        runListener(listener);

        return callbackId;
    }

    @Override
    public void removeListener(int listenerId) {
        if (valueListenerMap.containsKey(listenerId)) {
            valueListenerMap.remove(listenerId);
        }
        else {
            searchListenerMap.remove(listenerId);
        }
    }

    @Override
    public void delete(@NonNull String path, @NonNull Consumer<String> errorFunction) {
        delete(path, () -> {}, errorFunction);
    }

    @Override
    public void delete(@NonNull String path, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        try {
            List<String> keys = splitString(path, SLASH);
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
