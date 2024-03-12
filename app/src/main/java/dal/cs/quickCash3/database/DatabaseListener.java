package dal.cs.quickCash3.database;

import static dal.cs.quickCash3.database.DatabaseHelper.splitLocationIntoKeys;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

class DatabaseListener<T> {
    private final List<String> keys;
    private final Class<T> type;
    private final Consumer<T> readFunction;
    private final Consumer<String> errorFunction;

    public DatabaseListener(String location, Class<T> type, Consumer<T> readFunction, Consumer<String> errorFunction) {
        this.keys = splitLocationIntoKeys(location);
        this.type = type;
        this.readFunction = readFunction;
        this.errorFunction = errorFunction;
    }

    public boolean isLocation(List<String> keys) {
        if (this.keys.size() > keys.size()) {
            return false;
        }

        for (int i = 0; i < this.keys.size(); i++) {
            String ours = this.keys.get(i);
            String theirs = keys.get(i);
            if (!ours.equals(theirs)) {
                return false;
            }
        }

        return true;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void read(Object value) {
        if (!type.equals(value.getClass())) {
            throw new ClassCastException(String.format(Locale.getDefault(), "Cannot cast %s to %s", value.getClass(), type));
        }
        // This is completely safe.
        //noinspection unchecked
        readFunction.accept((T)value);
    }

    public void error(String error) {
        errorFunction.accept(error);
    }
}
