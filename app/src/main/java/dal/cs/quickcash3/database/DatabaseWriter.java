package dal.cs.quickcash3.database;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

/**
 * Interface for writing to a database with error handling and optional success handling.
 */
public interface DatabaseWriter {
    /**
     * Writes to the database with error handling.
     *
     * @param database The database to write to.
     * @param errorFunction The function to handle errors.
     * @return A string representing the result of the write operation.
     */
    @NonNull String writeToDatabase(
        @NonNull Database database,
        @NonNull Consumer<String> errorFunction);

    /**
     * Writes to the database with success and error handling.
     *
     * @param database The database to write to.
     * @param successFunction The function to handle success.
     * @param errorFunction The function to handle errors.
     * @return A string representing the result of the write operation.
     */
    @NonNull String writeToDatabase(
        @NonNull Database database,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction);

    /**
     * Writes to the database with a specific key and error handling.
     *
     * @param database The database to write to.
     * @param key The key to use for writing.
     * @param errorFunction The function to handle errors.
     */
    void writeToDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Consumer<String> errorFunction);

    /**
     * Writes to the database with a specific key, success, and error handling.
     *
     * @param database The database to write to.
     * @param key The key to use for writing.
     * @param successFunction The function to handle success.
     * @param errorFunction The function to handle errors.
     */
    void writeToDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction);
}
