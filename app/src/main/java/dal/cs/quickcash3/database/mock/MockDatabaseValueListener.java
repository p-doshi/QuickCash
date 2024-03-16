package dal.cs.quickcash3.database.mock;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.function.Consumer;

class MockDatabaseValueListener<T> extends MockDatabaseListener<T> {
    private final Consumer<T> readFunction;

    public MockDatabaseValueListener(
        @NonNull String location,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        super(location, type, errorFunction);
        this.readFunction = readFunction;
    }

    public void sendValue(@Nullable String key, @Nullable Object value) {
        if (!type.isInstance(value)) {
            throw new ClassCastException("Cannot cast " + value.getClass() + " to " + type);
        }
        readFunction.accept(type.cast(value));
    }
}
