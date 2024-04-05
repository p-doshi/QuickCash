package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public class CustomObserver<T> implements CollectionObserver<T> {
    private final Consumer<T> addFunction;
    private final Consumer<T> removeFunction;

    public CustomObserver(@NonNull Consumer<T> addFunction, @NonNull Consumer<T> removeFunction) {
        this.addFunction = addFunction;
        this.removeFunction = removeFunction;
    }

    @Override
    public void add(@NonNull T value) {
        addFunction.accept(value);
    }

    @Override
    public void remove(@NonNull T value) {
        removeFunction.accept(value);
    }
}
