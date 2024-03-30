package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * A thread-safe, single-use container that asynchronously captures and provides a value.
 * It acts as a synchronization aid that allows one or more threads to wait until
 * a set of operations being performed in other threads completes.
 *
 * @param <T> the type of the value being provided
 */
public class AsyncLatch<T> {
    private final AtomicReference<T> value = new AtomicReference<>(null);
    private final AtomicReference<Consumer<T>> readyFunction = new AtomicReference<>();

    /**
     * Construct a latch without a value or ready function.
     */
    public AsyncLatch() {}

    /**
     * Construct a latch with a value but no ready function.
     */
    public AsyncLatch(@NonNull T item) {
        value.set(item);
    }

    /**
     * Sets the value and triggers the ready function if it is present.
     * If the ready function is not set, it stores the value for future retrieval.
     *
     * @param item the value to be set
     */
    public void set(@NonNull T item) {
        if (readyFunction.get() == null) {
            value.set(item);
        } else {
            readyFunction.getAndSet(null).accept(item);
        }
    }

    /**
     * Registers a function to be called with the value once it is set.
     * If the value is already set, it calls the function immediately with the value.
     *
     * @param function the function to be executed with the value
     */
    public void get(@NonNull Consumer<T> function) {
        if (value.get() != null) {
            function.accept(value.getAndSet(null));
        } else {
            readyFunction.set(function);
        }
    }
}
