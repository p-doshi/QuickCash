package dal.cs.quickcash3.test;

import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ActivityMonitorRule<T extends Activity> implements TestRule {
    private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    private final Instrumentation.ActivityMonitor monitor;

    public ActivityMonitorRule(@NonNull Class<T> type) {
        monitor = new Instrumentation.ActivityMonitor(type.getName(), null, false);
    }

    public @NonNull Activity waitForActivity(int timeout) {
        Activity activity = monitor.waitForActivityWithTimeout(timeout);
        assertNotNull(activity);
        return activity;
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
