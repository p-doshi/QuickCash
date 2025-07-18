package dal.cs.quickcash3.database.mock;

import static dal.cs.quickcash3.util.StringHelper.SLASH;
import static dal.cs.quickcash3.util.StringHelper.splitString;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

abstract class MockDatabaseListener<T> {
    private final List<String> keys;
    protected final Class<T> type;
    private final Consumer<String> errorFunction;

    protected MockDatabaseListener(
        @NonNull String path,
        @NonNull Class<T> type,
        @NonNull Consumer<String> errorFunction)
    {
        this.keys = splitString(path, SLASH);
        this.type = type;
        this.errorFunction = errorFunction;
    }

    public boolean isPath(@NonNull List<String> keys) {
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

    public void sendError(@NonNull String error) {
        errorFunction.accept(error);
    }
}
