package dal.cs.temp.database;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public interface Database {
    /**
     * Asynchronously writes the value to the database.
     *
     * @param location The location in the database to write the value.
     * @param value The value to write to the database at the given location.
     * @param errorFunction The function that will be called in case of an error.
     * @param <T> Can write any type of data to firebase.
     */
    <T> void write(@NonNull String location, T value, @NonNull Consumer<String> errorFunction);

    /**
     * Asynchronously writes the value to the database.
     *
     * @param location The location in the database to write the value.
     * @param value The value to write to the database at the given location.
     * @param errorFunction The function that will be called in case of an error.
     * @param successFunction The function that will be called after the asynchronous operation
     *                        completes successfully.
     * @param <T> Can write any type of data to firebase.
     */
    <T> void write(@NonNull String location, T value, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction);

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
     * @param <T> Can read any type of data from firebase.
     */
    <T> void read(@NonNull String location, @NonNull Class<T> type, @NonNull Consumer<T> readFunction, @NonNull Consumer<String> errorFunction);
}
