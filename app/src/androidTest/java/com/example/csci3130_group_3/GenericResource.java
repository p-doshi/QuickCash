package com.example.csci3130_group_3;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class GenericResource implements IdlingResource {
    private final Supplier<Boolean> idleFunction;
    private ResourceCallback callback;

    /**
     * Creates an idle resource and attaches itself to the idling registry.
     * @param idleFunction Iff this function returns true, this resource will be marked as idle.
     */
    public GenericResource(Supplier<Boolean> idleFunction) {
        this.idleFunction = idleFunction;
    }

    @Override
    public String getName() {
        return "GenericResource";
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = idleFunction.get();
        if (idle && callback != null) {
            // Notify Espresso that the resource is idle
            callback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}
