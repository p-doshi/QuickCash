package dal.cs.quickcash3.database.firebase;

import static dal.cs.quickcash3.util.StringHelper.SLASH;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dal.cs.quickcash3.search.SearchFilter;

public class MyFirebaseDatabase extends MyFirebaseDatabaseImpl {
    private static final String DB_KEY = "nP5exoTNYnlqpPD1B3BHeuNDcWaPxI";

    public MyFirebaseDatabase(@NonNull Context context) {
        super(context);
    }

    protected @NonNull String relocate(@NonNull String location) {
        StringBuilder newLocation = new StringBuilder(DB_KEY);

        if (location.charAt(0) != SLASH) {
            newLocation.append(SLASH);
        }
        newLocation.append(location);

        return newLocation.toString();
    }

    @Override
    public <T> void read(
        @NonNull String location,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        super.read(relocate(location), type, readFunction, errorFunction);
    }

    @Override
    public <T> void write(
        @NonNull String location,
        @Nullable T value,
        @NonNull Consumer<String> errorFunction)
    {
        super.write(relocate(location), value, errorFunction);
    }

    @Override
    public <T> void write(
        @NonNull String location,
        @Nullable T value,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        super.write(relocate(location), value, successFunction, errorFunction);
    }

    @Override
    public <T> int addListener(
        @NonNull String location,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        return super.addListener(relocate(location), type, readFunction, errorFunction);
    }

    @Override
    public <T> int addSearchListener(
        @NonNull String location,
        @NonNull Class<T> type,
        @NonNull SearchFilter<T> filter,
        @NonNull BiConsumer<String, T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        return super.addSearchListener(relocate(location), type, filter, readFunction, errorFunction);
    }

    @Override
    public void delete(@NonNull String location, @NonNull Consumer<String> errorFunction) {
        super.delete(relocate(location), errorFunction);
    }

    @Override
    public void delete(
        @NonNull String location,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        super.delete(relocate(location), successFunction, errorFunction);
    }
}
