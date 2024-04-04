package dal.cs.quickcash3.database.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class FilteredChildEventListener<T> implements ChildEventListener {
    private final Class<T> type;
    private final BiConsumer<String, T> readFunction;
    private final Consumer<String> errorFunction;

    public FilteredChildEventListener(
        @NonNull Class<T> type,
        @NonNull BiConsumer<String, T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        this.type = type;
        this.readFunction = readFunction;
        this.errorFunction = errorFunction;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        String key = Objects.requireNonNull(snapshot.getKey());
        T value = Objects.requireNonNull(snapshot.getValue(type));
        readFunction.accept(key, value);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        String key = Objects.requireNonNull(snapshot.getKey());
        T value = snapshot.getValue(type);
        readFunction.accept(key, value);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        String key = Objects.requireNonNull(snapshot.getKey());
        readFunction.accept(key, null);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        throw new IllegalStateException("onChildMoved() not implemented");
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        errorFunction.accept(error.getMessage());
    }
}
