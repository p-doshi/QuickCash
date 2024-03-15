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
}
