package dal.cs.quickcash3.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import dal.cs.quickcash3.search.SearchFilter;
import dal.cs.quickcash3.util.CollectionObserver;

public class ObjectSearchAdapter<T extends DatabaseObject> {
    private final SearchFilter<T> filter;
    private final List<CollectionObserver<T>> lists = new ArrayList<>();
    private final Map<String, T> map = new TreeMap<>();

    public ObjectSearchAdapter(@NonNull SearchFilter<T> filter) {
        this.filter = filter;
    }

    public final void receive(@NonNull String path, @Nullable T value) {
        if (value == null) {
            if (map.containsKey(path)) {
                T oldValue = map.remove(path);
                assert oldValue != null;
                onDelete(oldValue);
            }
        }
        else {
            value.key(path);
            if (map.containsKey(path)) {
                map.put(path, value);
                onChange(value);
            }
            else if (filter.isValid(value)) {
                map.put(path, value);
                onAdd(value);
            }
        }
    }

    public void addObserver(@NonNull CollectionObserver<T> observer) {
        lists.add(observer);
    }

    public void removeObserver(@NonNull CollectionObserver<T> observer) {
        lists.remove(observer);
    }

    private void onAdd(@NonNull T value) {
        lists.forEach(list -> list.add(value));
    }

    private void onChange(@NonNull T value) {
        lists.forEach(list -> {
            list.remove(value);
            list.add(value);
        });
    }

    private void onDelete(@NonNull T value) {
        lists.forEach(list -> list.remove(value));
    }
}
