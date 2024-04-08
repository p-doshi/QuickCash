package dal.cs.quickcash3.search;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Function;

public class ListSearchFilter<T, V> extends SearchFilter<T> {
    private final Function<T, V> memberFunction;
    private List<V> list;

    public ListSearchFilter(@NonNull Function<T, V> memberFunction) {
        super();
        this.memberFunction = memberFunction;
    }

    public void setList(@NonNull List<V> list) {
        this.list = list;
    }

    @Override
    public boolean isCurrentValid(@NonNull T elem) {
        if (list == null) {
            throw new NullPointerException("Cannot apply " + this + " without a list");
        }

        V value = memberFunction.apply(elem);
        return list.contains(value);
    }
}
