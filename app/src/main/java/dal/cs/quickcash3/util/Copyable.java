package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

public interface Copyable<T> {
    /**
     * Copy the components from another object to this object.
     *
     * @param other The other object to copy from.
     */
    void copyFrom(@NonNull T other);
}
