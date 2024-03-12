package dal.cs.quickcash3.database;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public class MyFirebaseDatabase extends MyFirebaseDatabaseImpl {
    private static final char KEY_SEPARATOR = '/';
    private static final String DB_KEY = "nP5exoTNYnlqpPD1B3BHeuNDcWaPxI";

    public MyFirebaseDatabase(Context context) {
        super(context);
    }

    protected String relocate(String location) {
        StringBuilder newLocation = new StringBuilder(DB_KEY);

        if (location.charAt(0) != KEY_SEPARATOR) {
            newLocation.append(KEY_SEPARATOR);
        }
        newLocation.append(location);

        return newLocation.toString();

    }

    @Override
    public <T> void read(@NonNull String location, @NonNull Class<T> type, @NonNull Consumer<T> readFunction, @NonNull Consumer<String> errorFunction) {
        super.read(relocate(location), type, readFunction, errorFunction);
    }

    @Override
    public <T> void write(@NonNull String location, T value, @NonNull Consumer<String> errorFunction) {
        super.write(relocate(location), value, errorFunction);
    }

    @Override
    public <T> void write(@NonNull String location, T value, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        super.write(relocate(location), value, successFunction, errorFunction);
    }

    @Override
    public <T> int addListener(@NonNull String location, @NonNull Class<T> type, @NonNull Consumer<T> readFunction, @NonNull Consumer<String> errorFunction) {
        return super.addListener(relocate(location), type, readFunction, errorFunction);
    }

    @Override
    public void delete(@NonNull String location, @NonNull Consumer<String> errorFunction) {
        super.delete(relocate(location), errorFunction);
    }

    @Override
    public void delete(@NonNull String location, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        super.delete(relocate(location), successFunction, errorFunction);
    }
}
