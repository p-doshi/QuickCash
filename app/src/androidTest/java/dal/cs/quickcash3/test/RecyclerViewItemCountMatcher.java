package dal.cs.quickcash3.test;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class RecyclerViewItemCountMatcher extends TypeSafeMatcher<View> {
    private final int count;

    public RecyclerViewItemCountMatcher(int count) {
        super();
        this.count = count;
    }

    @Override
    protected boolean matchesSafely(View item) {
        RecyclerView recyclerView = (RecyclerView) item;
        //noinspection rawtypes
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        return adapter != null && adapter.getItemCount() == count;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("RecyclerView should have " + count + " items");
    }

    public static Matcher<View> hasItemCount(int count) {
        return new RecyclerViewItemCountMatcher(count);
    }
}
