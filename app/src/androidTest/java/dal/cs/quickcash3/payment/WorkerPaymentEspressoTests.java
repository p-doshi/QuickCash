package dal.cs.quickcash3.payment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Instrumentation;
import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dal.cs.quickcash3.R;

@RunWith(AndroidJUnit4.class)
public class WorkerPaymentEspressoTests {
    private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    private final Instrumentation.ActivityMonitor monitor = new Instrumentation.ActivityMonitor(
            WorkerPaymentConfirmationActivity.class.getName(), null, false);
    private static final int MAX_TIMEOUT = 10000;

    @Before
    public void setup() {
        ActivityScenario.launch(WorkerPayPalActivity.class);
        instrumentation.addMonitor(monitor);
    }

    @After
    public void teardown() {
        instrumentation.removeMonitor(monitor);
    }

    @Test
    public void showPayConfirmationButton(){
        onView(withId(R.id.seePayStatus)).perform(click());
    }

    @Test
    public void showPaymentStatus() {
        SystemClock.sleep(100);
        onView(withId(R.id.seePayStatus)).perform(click());
        monitor.waitForActivityWithTimeout(MAX_TIMEOUT);
        onView(withId(R.id.workerStatusMessage)).check(matches(withText("approved")));
        onView(withId(R.id.workerPayID)).check(matches(withText("Hello Yuki")));
    }
}
