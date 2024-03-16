package dal.cs.quickcash3.database;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public interface DatabaseWriter {
    /**
     * Writes this object to the database.
     *
     * @param database The database to write to.
     * @param errorFunction The function that is called in case of an error.
     */
    void writeToDatabase(@NonNull Database database, @NonNull Consumer<String> errorFunction);

    /**
     * Writes this object to the database and executes the successFunction upon successful completion.
     *
     * @param database The database to write to.
     * @param successFunction The function that is called upon successful completion of the write operation.
     * @param errorFunction The function that is called in case of an error during the write operation.
     */
    void writeToDatabase(
        @NonNull Database database,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction);
}
