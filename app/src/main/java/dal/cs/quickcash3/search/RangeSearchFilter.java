package dal.cs.quickcash3.search;

import androidx.annotation.NonNull;

import java.util.function.Function;

import dal.cs.quickcash3.util.Range;

public class RangeSearchFilter<T, V extends Comparable<? super V>> extends SearchFilter<T> {
    private final Function<T, V> memberFunction;
    private Range<V> range;

    public RangeSearchFilter(@NonNull Function<T, V> memberFunction) {
        super();
        this.memberFunction = memberFunction;
    }

    public void setRange(@NonNull Range<V> range) {
        this.range = range;
    }

    @Override
    public boolean isCurrentValid(@NonNull T elem) {
        if (range == null) {
            throw new NullPointerException("Cannot apply " + this + " without a pattern");
        }

        V value = memberFunction.apply(elem);
        return range.contains(value);
    }
}
