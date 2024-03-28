package dal.cs.quickcash3.test;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;

import org.hamcrest.Matcher;

public class ScrollAboveKeyboardAction implements ViewAction {
    private static final String LOG_TAG = ScrollAboveKeyboardAction.class.getSimpleName();

    @Override
    public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(View.class);
    }

    @Override
    public String getDescription() {
        return "Custom scrollTo action";
    }

    @Override
    public void perform(UiController uiController, View view) {
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);

        // Calculate the visible height as a percentage of the total view height
        float visibleHeightPercentage = (float) rect.height() / view.getHeight();

        if (visibleHeightPercentage >= 0.9) {
            Log.i(LOG_TAG, "View is already displayed. Returning.");
            return;
        }

        if (!view.requestRectangleOnScreen(rect, true /* immediate */)) {
            Log.w(LOG_TAG, "Scrolling to view was requested, but none of the parents scrolled.");
        }

        uiController.loopMainThreadUntilIdle();

        if (visibleHeightPercentage < 0.9) {
            throw new PerformException.Builder()
                .withActionDescription(this.getDescription())
                .withViewDescription(HumanReadables.describe(view))
                .withCause(new RuntimeException("Scrolling to view was attempted, but the view is not displayed"))
                .build();
        }
    }

    public static ViewAction scrollAboveKeyboard() {
        return new ScrollAboveKeyboardAction();
    }
}
