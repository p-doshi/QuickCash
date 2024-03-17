package dal.cs.quickcash3.database.mock;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dal.cs.quickcash3.search.SearchFilter;

class MockDatabaseSearchListener<T> extends MockDatabaseListener<T> {
    private final BiConsumer<String, T> readFunction;
    private final SearchFilter<T> filter;
    private final Map<String, Object> map = new MockDatabase.MapType();

    public MockDatabaseSearchListener(
        @NonNull String location,
        @NonNull Class<T> type,
        @NonNull SearchFilter<T> filter,
        @NonNull BiConsumer<String, T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        super(location, type, errorFunction);
        this.filter = filter;
        this.readFunction = readFunction;
    }

    private void read(@Nullable String key, @Nullable Object object) {
        if (object != null && !type.isInstance(object)) {
            throw new ClassCastException("Cannot cast " + object.getClass() + " to " + type);
        }

        T value = type.cast(object);
        if (value == null || filter.isValid(value)) {
            readFunction.accept(key, value);
        }
    }

    public void update(@NonNull Map<String, Object> newMap) {
        List<String> keysToRemove = new ArrayList<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            // Check for removed keys.
            if (!newMap.containsKey(entry.getKey())) {
                read(entry.getKey(), null);
                keysToRemove.add(entry.getKey());
            }
            else {
                // Check for modified values.
                Object newValue = newMap.get(entry.getKey());
                if (!entry.getValue().equals(newValue)) {
                    read(entry.getKey(), newValue);
                    map.put(entry.getKey(), newValue);
                }
            }
        }

        for (String key : keysToRemove) {
            map.remove(key);
        }

        for (Map.Entry<String, Object> entry : newMap.entrySet()) {
            // Add new ones.
            if (!map.containsKey(entry.getKey())) {
                read(entry.getKey(), entry.getValue());
                map.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
