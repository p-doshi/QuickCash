package dal.cs.quickcash3.test;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static org.hamcrest.Matchers.anyOf;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;

import org.hamcrest.Matcher;

public class ScrollAboveKeyboardAction implements ViewAction {
    private static final String LOG_TAG = ScrollAboveKeyboardAction.class.getSimpleName();

    @Override
    public Matcher<View> getConstraints() {
        return isDescendantOfA(
                anyOf(
                        isAssignableFrom(ScrollView.class),
                        isAssignableFrom(NestedScrollView.class),
                        isAssignableFrom(RecyclerView.class)));
    }

    @Override
    public String getDescription() {
        return "Custom scrollTo action";
    }

    private static float rectAreaOverlap(Rect rect1, Rect rect2) {
        // Calculate the intersection rectangle
        int left = Math.max(rect1.left, rect2.left);
        int right = Math.min(rect1.right, rect2.right);
        int top = Math.max(rect1.top, rect2.top);
        int bottom = Math.min(rect1.bottom, rect2.bottom);

        // Check if there's any overlap
        if (left >= right || top >= bottom) {
            return 0.0f; // No overlap
        }

        // Calculate the area of the intersection rectangle
        int width = right - left;
        int height = bottom - top;
        return width * height;
    }

    private static boolean isDisplayingAtLeast(View view, @FloatRange(from=0.0f, to=1.0f) float percentage) {
        Rect displayRect = new Rect();
        view.getWindowVisibleDisplayFrame(displayRect);

        Rect viewRect = new Rect();
        view.getGlobalVisibleRect(viewRect);

        float overlappingArea = rectAreaOverlap(displayRect, viewRect);
        float area = viewRect.height() * viewRect.width();

        return overlappingArea >= percentage * area;
    }

    private static void scrollIntoView(View view) {
        Rect displayRect = new Rect();
        view.getWindowVisibleDisplayFrame(displayRect);

        Rect viewRect = new Rect();
        view.getGlobalVisibleRect(viewRect);

        int heightDifference = viewRect.bottom - displayRect.bottom;

        View parent = (View) view.getParent();
        parent.scrollBy(0, heightDifference);
    }

    @Override
    public void perform(@Nullable UiController uiController, @NonNull View view) {
        if (isDisplayingAtLeast(view, 0.9f)) {
            Log.i(LOG_TAG, "View is already displayed. Returning.");
            return;
        }

        scrollIntoView(view);

        assert uiController != null;
        uiController.loopMainThreadUntilIdle();

        if (!isDisplayingAtLeast(view, 0.9f)) {
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
