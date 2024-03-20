package dal.cs.quickcash3.database.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dal.cs.quickcash3.search.SearchFilter;

class FilteredChildEventListener<T> implements ChildEventListener {
    private final Class<T> type;
    private final SearchFilter<T> filter;
    private final BiConsumer<String, T> readFunction;
    private final Consumer<String> errorFunction;

    public FilteredChildEventListener(
        @NonNull Class<T> type,
        @NonNull SearchFilter<T> filter,
        @NonNull BiConsumer<String, T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        this.type = type;
        this.filter = filter;
        this.readFunction = readFunction;
        this.errorFunction = errorFunction;
    }

    private void add(@Nullable String key, @Nullable T value) {
        if (value == null || filter.isValid(value)) {
            readFunction.accept(key, value);
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        String key = snapshot.getKey();
        T value = snapshot.getValue(type);
        add(key, value);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        String key = snapshot.getKey();
        T value = snapshot.getValue(type);
        add(key, value);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        String key = snapshot.getKey();
        add(key, null);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        String key = snapshot.getKey();
        T value = snapshot.getValue(type);
        add(previousChildName, null);
        add(key, value);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        errorFunction.accept(error.getMessage());
    }
}
