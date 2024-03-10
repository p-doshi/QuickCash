package dal.cs.temp.locationAndroidTests;

import static org.hamcrest.CoreMatchers.any;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;

public final class WaitForAction implements ViewAction {
    private final Matcher<View> matcher;
    private final long timeout;

    private WaitForAction(@NonNull Matcher<View> matcher, long timeout) {
        this.matcher = matcher;
        this.timeout = timeout;
    }

    @Override
    public Matcher<View> getConstraints() {
        return any(View.class);
    }

    @Override
    public String getDescription() {
        return "Wait up to " + timeout + " milliseconds for " + matcher;
    }

    @Override
    public void perform(UiController uiController, View view) {
        long endTime = System.currentTimeMillis() + timeout;

        do {
            if (matcher.matches(view)) {
                return;
            }
            uiController.loopMainThreadForAtLeast(50);
        } while (System.currentTimeMillis() < endTime);

        throw new PerformException.Builder()
            .withActionDescription(getDescription())
            .withCause(new TimeoutException("Waited " + timeout + " milliseconds"))
            .withViewDescription(HumanReadables.describe(view))
            .build();
    }

    public static ViewAction waitFor(@NonNull Matcher<View> matcher, long timeout) {
        return new WaitForAction(matcher, timeout);
    }
}
