package dal.cs.quickcash3.search;

import androidx.annotation.NonNull;

public abstract class SearchFilter<T> {
    private SearchFilter<T> nextFilter;

    protected abstract boolean isCurrentValid(@NonNull T elem);

    /**
     * Set the next filter to be applied after this one.
     *
     * @param nextFilter The filter that will be applied after this one.
     * @return The next filter. This allows chaining a set of filters together easily.
     */
    public @NonNull SearchFilter<T> addNext(@NonNull SearchFilter<T> nextFilter) {
        if (this.equals(nextFilter)) {
            throw new IllegalArgumentException("Cannot set the next search filter cannot be the current");
        }
        this.nextFilter = nextFilter;
        return nextFilter;
    }

    /**
     * Check if the given value passes the search filter.
     *
     * @param value The value to check against.
     * @return True if the value passes the filter; otherwise, false.
     */
    public final boolean isValid(@NonNull T value) {
        for (SearchFilter<T> currentFilter = this;
             currentFilter != null;
             currentFilter = currentFilter.nextFilter)
        {
            if (!currentFilter.isCurrentValid(value)) {
                return false;
            }
        }

        return true;
    }
}
