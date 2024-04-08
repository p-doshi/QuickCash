package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

public interface CollectionObserver<T> {
    /**
     * Add the value to the collection.
     *
     * @param value The value to add.
     */
    void add(@NonNull T value);

    /**
     * Remove the value from the collection.
     *
     * @param value The value to remove.
     */
    void remove(@NonNull T value);
}
