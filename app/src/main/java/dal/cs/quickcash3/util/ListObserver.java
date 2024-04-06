package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import java.util.List;

public class ListObserver<T> implements CollectionObserver<T> {
    private final List<T> list;

    public ListObserver(@NonNull List<T> list) {
        this.list = list;
    }

    @Override
    public void add(@NonNull T value) {
        list.add(value);
    }

    @Override
    public void remove(@NonNull T value) {
        list.remove(value);
    }
}
