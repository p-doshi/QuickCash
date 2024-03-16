package dal.cs.quickcash3.database.mock;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dal.cs.quickcash3.search.SearchFilter;

class MockDatabaseSearchListener<T> extends MockDatabaseListener<T> {
    private final BiConsumer<String, T> readFunction;
    private final SearchFilter<T> filter;

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

    public void sendValue(@Nullable String key, @Nullable Object value) {
        if (!type.isInstance(value)) {
            throw new ClassCastException("Cannot cast " + value.getClass() + " to " + type);
        }
        
        T tValue = type.cast(value);
        if (tValue == null || filter.isValid(tValue)) {
            readFunction.accept(key, tValue);
        }
    }
}
