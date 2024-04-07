package dal.cs.quickcash3.search;

import androidx.annotation.NonNull;

import java.util.function.Function;
import java.util.regex.Pattern;

public class RegexSearchFilter<T> extends SearchFilter<T> {
    private final Function<T, String> stringFunction;
    private Pattern pattern;

    public RegexSearchFilter(@NonNull Function<T, String> stringFunction) {
        super();
        this.stringFunction = stringFunction;
    }

    public void setPattern(@NonNull Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean isCurrentValid(@NonNull T elem) {
        if (pattern == null) {
            throw new NullPointerException("Cannot apply " + this + " without a pattern");
        }

        String value = stringFunction.apply(elem);
        return pattern.matcher(value).matches();
    }
}
