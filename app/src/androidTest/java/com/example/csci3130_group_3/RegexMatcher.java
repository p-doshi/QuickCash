package com.example.csci3130_group_3;

import java.util.regex.Pattern;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import android.view.View;
import android.widget.TextView;
import androidx.test.espresso.matcher.BoundedMatcher;

class RegexMatcher extends BoundedMatcher<View, TextView> {
    private final String regex;
    private final Pattern pattern;

    public RegexMatcher(String regex) {
        super(TextView.class);
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Checking the matcher on received view: with pattern=" + regex);
    }

    @Override
    public boolean matchesSafely(TextView item) {
        return item != null && item.getText() != null && pattern.matcher(item.getText()).matches();
    }

    public static Matcher<View> withPattern(String regex) {
        return new RegexMatcher(regex);
    }
}
