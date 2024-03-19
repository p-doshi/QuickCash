package dal.cs.quickcash3.database;

import static dal.cs.quickcash3.database.DatabaseHelper.splitLocationIntoKeys;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

class DatabaseListener<T> {
    private final List<String> keys;
    private final Class<T> type;
    private final Consumer<T> readFunction;
    private final Consumer<String> errorFunction;

    public DatabaseListener(@NonNull String location, @NonNull Class<T> type, @NonNull Consumer<T> readFunction, @NonNull Consumer<String> errorFunction) {
        this.keys = splitLocationIntoKeys(location);
        this.type = type;
        this.readFunction = readFunction;
        this.errorFunction = errorFunction;
    }

    public boolean isLocation(@NonNull List<String> keys) {
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

    public void sendValue(@Nullable Object value) {
        if (!type.isInstance(value)) {
            throw new ClassCastException("Cannot cast " + value.getClass() + " to " + type);
        }
        readFunction.accept(type.cast(value));
    }

    public void sendError(@NonNull String error) {
        errorFunction.accept(error);
    }
}
