package dal.cs.quickcash3.database;

import androidx.annotation.NonNull;

import com.google.errorprone.annotations.CheckReturnValue;

import java.util.function.Consumer;

public interface DatabaseObject {
    /**
     * Writes this object to the database.
     *
     * @param database The database to write to.
     * @param errorFunction The function that is called in case of an error.
     * @return Returns the key that the job was written to.
     */
    @CheckReturnValue
    @NonNull String writeToDatabase(@NonNull Database database, @NonNull Consumer<String> errorFunction);

    /**
     * Writes this object to the database and executes the successFunction upon successful completion.
     *
     * @param database The database to write to.
     * @param successFunction The function that is called upon successful completion of the write operation.
     * @param errorFunction The function that is called in case of an error during the write operation.
     * @return Returns the key that the job was written to.
     */
    @CheckReturnValue
    @NonNull String writeToDatabase(
        @NonNull Database database,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction);

    /**
     * Reads this object from its known location with the given key in the database.
     *
     * @param database The database to read from.
     * @param key The key for the particular object to read.
     * @param errorFunction The function that is called in case of an error.
     */
    void readFromDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Consumer<String> errorFunction);

    /**
     * Reads this object from its known location with the given key in the database and executes the
     * successFunction upon successful completion.
     *
     * @param database The database to read from.
     * @param key The key for the particular object to read.
     * @param successFunction The function that is called upon successful completion of the read operation.
     * @param errorFunction The function that is called in case of an error.
     */
    void readFromDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction);
}
