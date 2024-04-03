package dal.cs.quickcash3.test;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.regex.Pattern;

public final class RegexMatcher extends BoundedMatcher<View, TextView> {
    private final Pattern pattern;

    private RegexMatcher(Pattern regex) {
        super(TextView.class);
        this.pattern = regex;
    }

    private RegexMatcher(String regex) {
        super(TextView.class);
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("pattern=" + pattern.pattern());
    }

    @Override
    public boolean matchesSafely(@NonNull TextView item) {
        return pattern.matcher(item.getText()).matches();
    }

    public static Matcher<View> withPattern(Pattern regex) {
        return new RegexMatcher(regex);
    }

    public static Matcher<View> withPattern(String regex) {
        return new RegexMatcher(regex);
    }
}
