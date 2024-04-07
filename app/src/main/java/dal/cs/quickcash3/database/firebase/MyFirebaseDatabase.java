package dal.cs.quickcash3.database.firebase;

import static dal.cs.quickcash3.util.StringHelper.SLASH;

import androidx.annotation.NonNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MyFirebaseDatabase extends MyFirebaseDatabaseImpl {
    private static final String DB_KEY = "nP5exoTNYnlqpPD1B3BHeuNDcWaPxI";

    protected @NonNull String relocate(@NonNull String path) {
        StringBuilder newPath = new StringBuilder(DB_KEY);

        if (path.charAt(0) != SLASH) {
            newPath.append(SLASH);
        }
        newPath.append(path);

        return newPath.toString();
    }

    @Override
    public <T> void read(
        @NonNull String path,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        super.read(relocate(path), type, readFunction, errorFunction);
    }

    @Override
    public <T> void write(
        @NonNull String path,
        @NonNull T value,
        @NonNull Consumer<String> errorFunction)
    {
        super.write(relocate(path), value, errorFunction);
    }

    @Override
    public <T> void write(
        @NonNull String path,
        @NonNull T value,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        super.write(relocate(path), value, successFunction, errorFunction);
    }

    @Override
    public <T> int addListener(
        @NonNull String path,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        return super.addListener(relocate(path), type, readFunction, errorFunction);
    }

    @Override
    public <T> int addDirectoryListener(
        @NonNull String path,
        @NonNull Class<T> type,
        @NonNull BiConsumer<String, T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        return super.addDirectoryListener(relocate(path), type, readFunction, errorFunction);
    }

    @Override
    public void delete(@NonNull String path, @NonNull Consumer<String> errorFunction) {
        super.delete(relocate(path), errorFunction);
    }

    @Override
    public void delete(
        @NonNull String path,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        super.delete(relocate(path), successFunction, errorFunction);
    }
}
