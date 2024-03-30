package dal.cs.quickcash3.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A TestRule implementation for monitoring activities.
 *
 * @param <T> the type of Activity to monitor
 */
public class ActivityMonitorRule<T extends Activity> implements TestRule {
    private final Class<T> type;
    private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    private final Instrumentation.ActivityMonitor monitor;

    public ActivityMonitorRule(@NonNull Class<T> type) {
        this.type = type;
        monitor = new Instrumentation.ActivityMonitor(type.getName(), null, false);
    }

    /**
     * Waits for the specified activity to be launched within the given timeout.
     *
     * @param timeout the maximum time to wait for the activity to be launched
     * @return the launched activity
     */
    public @NonNull T waitForActivity(int timeout) {
        Activity activity = monitor.waitForActivityWithTimeout(timeout);
        assertNotNull(activity);

        assertTrue(type.isInstance(activity));
        T myActivity = type.cast(activity);

        assertNotNull(myActivity);
        return myActivity;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                instrumentation.addMonitor(monitor);
                try {
                    base.evaluate(); // Run the test
                } finally {
                    instrumentation.removeMonitor(monitor);
                }
            }
        };
    }
}
