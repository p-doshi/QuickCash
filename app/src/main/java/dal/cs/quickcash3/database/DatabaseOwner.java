package dal.cs.quickcash3.database;

import androidx.annotation.NonNull;

public interface DatabaseOwner {
    /**
     * Get the database from its owner.
     *
     * @return The database.
     */
    @NonNull Database getDatabase();
}
