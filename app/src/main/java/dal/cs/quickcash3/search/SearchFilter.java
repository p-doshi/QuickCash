package dal.cs.quickcash3.search;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public abstract class SearchFilter<T> {
    protected final Gson gson = new Gson();
    private SearchFilter<T> nextFilter;

    protected abstract boolean isCurrentValid(@NonNull final JsonElement root);

    /**
     * Set the next filter to be applied after this one.
     *
     * @param nextFilter The filter that will be applied after this one.
     * @return The next filter. This allows chaining a set of filters together easily.
     */
    public @NonNull SearchFilter<T> setNext(@NonNull SearchFilter<T> nextFilter) {
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
        JsonElement root = gson.toJsonTree(value);
        return isCurrentValid(root) && (nextFilter == null || nextFilter.isCurrentValid(root));
    }
}
