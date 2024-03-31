package dal.cs.quickcash3.test;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A test rule for managing a counting resource.
 */
public final class CountingResourceRule implements TestRule {
    private final IdlingRegistry registry = IdlingRegistry.getInstance();
    private final String name;
    private CountingIdlingResource resource;

    private CountingResourceRule(@NonNull String name) {
        this.name = name;
    }

    /**
     * Increment the counting resource.
     */
    public void increment() {
        resource.increment();
    }

    /**
     * Decrement the counting resource.
     */
    public void decrement() {
        resource.decrement();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                resource = new CountingIdlingResource(name);
                registry.register(resource);
                try {
                    base.evaluate(); // Run the test
                } finally {
                    registry.unregister(resource);
                }
            }
        };
    }
}
