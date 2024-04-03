package dal.cs.quickcash3.test;

import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class SiblingMatcher extends TypeSafeMatcher<View> {
    private final Matcher<View> matcher;

    private SiblingMatcher(Matcher<View> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with neighbour: ");
        matcher.describeTo(description);
    }

    @Override
    public boolean matchesSafely(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent == null) return false;

        int index = parent.indexOfChild(view);
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (i != index && matcher.matches(parent.getChildAt(i))) {
                return true;
            }
        }

        return false;
    }

    public static Matcher<View> withSibling(Matcher<View> matcher) {
        return new SiblingMatcher(matcher);
    }
}
