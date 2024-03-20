package dal.cs.quickcash3.payment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
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
        onView(ViewMatchers.withId(R.id.seePayStatus)).perform(click());
    }

    @Test
    public void showPaymentStatus() {
        onView(withId(R.id.seePayStatus)).perform(click());
        monitor.waitForActivityWithTimeout(5000);
        onView(withId(R.id.workerStatusMessage)).check(matches(withText("approved")));
        onView(withId(R.id.workerPayID)).check(matches(withText("Hello Yuki")));
    }
}
