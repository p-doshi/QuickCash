package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * A thread-safe class that represents a promise for all future values of type T.
 *
 * @param <T> The type of the promised value.
 */
public class Promise<T> {
    private final AtomicReference<List<Consumer<T>>> pendingReadyFunctions = new AtomicReference<>(new ArrayList<>());
    private final AtomicReference<T> promisedValue = new AtomicReference<>(null);

    /**
     * Checks if the promised value is ready.
     *
     * @return true if the promised value is ready, false otherwise.
     */
    public boolean isReady() {
        return promisedValue.get() != null;
    }

    /**
     * Fulfills the promise with the given item and triggers all pending functions.
     *
     * @param item The item to fulfill the promise with.
     */
    public void fulfill(@NonNull T item) {
        promisedValue.set(item);
        List<Consumer<T>> functions = pendingReadyFunctions.get();
        for (Consumer<T> function : functions) {
            function.accept(item);
        }
    }

    /**
     * Runs the provided function when the promised value is ready and will use it for all future
     * values as well.
     *
     * @param function The function to run when the promised value is ready.
     */
    public void setUpdateCallback(@NonNull Consumer<T> function) {
        if (isReady()) {
            function.accept(promisedValue.get());
        }
        pendingReadyFunctions.get().add(function);
    }
}
