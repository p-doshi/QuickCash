package dal.cs.quickCash3.database;

import android.content.Context;

import java.util.function.Consumer;

public class MyFirebaseDatabase extends MyFirebaseDatabaseImpl {
    private static final String dbKey = "nP5exoTNYnlqpPD1B3BHeuNDcWaPxI";

    public MyFirebaseDatabase(Context context) {
        super(context);
    }

    protected String relocate(String location) {
        String newLocation = "/" + dbKey;
        if (!location.startsWith("/")) {
            newLocation += "/";
        }
        newLocation += location;
        return newLocation;
    }

    @Override
    public <T> void read(String location, Class<T> type, Consumer<T> readFunction, Consumer<String> errorFunction) {
        super.read(relocate(location), type, readFunction, errorFunction);
    }

    @Override
    public <T> void write(String location, T value, Consumer<String> errorFunction) {
        super.write(relocate(location), value, errorFunction);
    }

    @Override
    public <T> void write(String location, T value, Runnable successFunction, Consumer<String> errorFunction) {
        super.write(relocate(location), value, successFunction, errorFunction);
    }
}
