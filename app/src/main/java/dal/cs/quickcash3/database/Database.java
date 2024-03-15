package dal.cs.quickcash3.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.errorprone.annotations.CheckReturnValue;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dal.cs.quickcash3.search.SearchFilter;

public interface Database {
    /**
     * Asynchronously writes the value to the database.
     * Iff an error occurs, then the errorFunction will be called with the specific error.
     * In case of an error, the value will not be written to the database.
     *
     * @param location The location in the database to write the value.
     * @param value The value to write to the database at the given location.
     * @param errorFunction The function that will be called in case of an error.
     * @param <T> Can write any type of data to the database.
     */
    <T> void write(@NonNull String location, @Nullable T value, @NonNull Consumer<String> errorFunction);

    /**
     * Asynchronously writes the value to the database.
     * Iff an error occurs, then the errorFunction will be called with the specific error.
     * In case of an error, the value will not be written to the database and the successFunction
     * will not called.
     *
     * @param location The location in the database to write the value.
     * @param value The value to write to the database at the given location.
     * @param successFunction The function that will be called after the asynchronous operation
     *                        completes successfully.
     * @param errorFunction The function that will be called in case of an error.
     * @param <T> Can write any type of data to the database.
     */
    <T> void write(
        @NonNull String location,
        @Nullable T value,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction);

    /**
     * Asynchronously reads the value to the database.
     * Iff an error occurs, then the errorFunction will be called with the specific error.
     * Otherwise, errorFunction will not be called and the readFunction will be given the data from
     * the database.
     *
     * @param location The location in the database to read the value.
     * @param type The type of the data to read. NOTE: This is necessary due to Java's type erasure.
     * @param readFunction The function to receive the data.
     * @param errorFunction The function that will be called in case of an error.
     * @param <T> Can read any type of data from the database.
     */
    <T> void read(
        @NonNull String location,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction);

    /**
     * Set a listener for a value in the database at the given location. The readFunction will
     * be called after a successful setup and then again every time the value in the database is
     * modified. Iff an error occurs, the error function will be called and the read function will
     * never be called again.
     * This function will return an ID that can be passed to the removeListener function to stop
     * listening.
     *
     * @param location The location in the database to read the value.
     * @param type The type of the data to read. NOTE: This is necessary due to Java's type erasure.
     * @param readFunction The function to receive the data.
     * @param errorFunction The function that will be called in case of an error.
     * @return The ID for this listener.
     * @param <T> Can read any type of data from the database.
     */
    @CheckReturnValue
    <T> int addListener(
        @NonNull String location,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction);

    /**
     * Asynchronously searches for all values at the given location in database that match the
     * search filter. All of the search results will be passed to the read function individually
     * alongside their sub location. The sub location is the nested location of the data within the
     * given location. If data is removed, the read function will be called with its sub location
     * and a null pointer. If the search filter is changed while the listener is active, it results
     * in undefined behaviour. If an error occurs, then the errorFunction will be called with the
     * specific error.
     *
     * @param location The location in the database to search.
     * @param type The type of the data to read. NOTE: This is necessary due to Java's type erasure.
     * @param filter The search filter to use to search inside the location.
     *               DO NOT MODIFY THE FILTER WHILE LISTENER IS ACTIVE.
     * @param readFunction The function to receive the sub location and data at that sub location.
     * @param errorFunction The function that will be called in case of an error.
     * @param <T> Can read any type of data from the database.
     */
    @CheckReturnValue
    <T> int addSearchListener(
        @NonNull String location,
        @NonNull Class<T> type,
        @NonNull SearchFilter<T> filter,
        @NonNull BiConsumer<String, T> readFunction,
        @NonNull Consumer<String> errorFunction);

    /**
     * Remove the listener with the matching listenerId.
     *
     * @param listenerId The ID of the listener callback to remove.
     */
    void removeListener(int listenerId);

    /**
     * Delete the item in the database at the given location. Iff an error occurs, the error
     * function will be called and the read function will never be called again.
     *
     * @param location The location of the item to delete.
     * @param errorFunction The function that will be called in case of an error.
     */
    void delete(@NonNull String location, @NonNull Consumer<String> errorFunction);

    /**
     * Delete the item in the database at the given location. Iff an error occurs, the error
     * function will be called and the read function will never be called again.
     *
     * @param location The location of the item to delete.
     * @param successFunction The function that will be called on successful deletion.
     * @param errorFunction The function that will be called in case of an error.
     */
    void delete(
        @NonNull String location,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction);
}
