package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

/**
 * A range of any comparable type. The start and end are both inclusive.
 * NOTE: This we use this class instead of the android.util one because we need it in unit tests.
 *
 * @param <T> Any comparable type.
 */
public class Range<T extends Comparable<? super T>> {
    private final T start;
    private final T end;

    public Range(@NonNull T start, @NonNull T end) {
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException("Start cannot be larger than end");
        }
        this.start = start;
        this.end = end;
    }

    /**
     * Checks if the given value is inside of this range.
     *
     * @param value The value to compare against.
     * @return True if the value is within the range; otherwise, false.
     */
    public boolean contains(@NonNull T value) {
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    public @NonNull T getStart() {
        return start;
    }

    public @NonNull T getEnd() {
        return end;
    }
}
