package dal.cs.quickcash3.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

import dal.cs.quickcash3.data.User;

/**
 * Interface for writing to a database with error handling and optional success handling.
 */
public abstract class DatabaseObject {
    private String mKey;

    /**
     * Get the key for this object.
     *
     * @return The key for the object.
     */
    public @Nullable String key() {
        return mKey;
    }

    /**
     * Set the key for this object.
     *
     * @param key The key for the object.
     */
    protected void key(@NonNull String key) {
        this.mKey = key;
    }

    /**
     * Writes to the database with error handling.
     *
     * @param database The database to write to.
     * @param errorFunction The function to handle errors.
     */
    public abstract void writeToDatabase(
        @NonNull Database database,
        @NonNull Consumer<String> errorFunction);

    /**
     * Writes to the database with success and error handling.
     *
     * @param database The database to write to.
     * @param successFunction The function to handle success.
     * @param errorFunction The function to handle errors.
     */
    public abstract void writeToDatabase(
        @NonNull Database database,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction);

    /**
     * Deletes this object from the database.
     *
     * @param database The database to delete from.
     * @param errorFunction The function to handle errors.
     */
    public abstract void deleteFromDatabase(
        @NonNull Database database,
        @NonNull Consumer<String> errorFunction);

    @Override
    public final int hashCode() {
        return mKey.hashCode();
    }

    @Override
    public final boolean equals(@Nullable Object obj) {
        if (obj instanceof DatabaseObject) {
            DatabaseObject other = (DatabaseObject) obj;
            return Objects.equals(mKey, other.mKey);
        }
        return false;
    }
}
