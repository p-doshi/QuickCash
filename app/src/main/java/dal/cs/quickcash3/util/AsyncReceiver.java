package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public class AsyncReceiver<T> {
    private final Consumer<T> valueFunction;
    private final Consumer<String> errorFunction;

    public AsyncReceiver(@NonNull Consumer<T> valueFunction, @NonNull Consumer<String> errorFunction) {
        this.valueFunction = valueFunction;
        this.errorFunction = errorFunction;
    }

    public void receiveValue(@NonNull T value) {
        valueFunction.accept(value);
    }

    public void receiveError(@NonNull String error) {
        errorFunction.accept(error);
    }
}
