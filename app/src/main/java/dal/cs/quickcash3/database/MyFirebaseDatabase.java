package dal.cs.quickcash3.database;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public class MyFirebaseDatabase extends MyFirebaseDatabaseImpl {
    private static final String DB_KEY = "nP5exoTNYnlqpPD1B3BHeuNDcWaPxI";

    public MyFirebaseDatabase(@NonNull Context context) {
        super(context);
    }

    protected @NonNull String relocate(@NonNull String location) {
        StringBuilder newLocation = new StringBuilder('/');
        newLocation.append(DB_KEY);
        if (location.charAt(0) != '/') {
            newLocation.append('/');
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
}
