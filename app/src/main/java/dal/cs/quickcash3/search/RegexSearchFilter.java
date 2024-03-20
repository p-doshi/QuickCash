package dal.cs.quickcash3.search;

import static dal.cs.quickcash3.util.GsonHelper.getAt;
import static dal.cs.quickcash3.util.StringHelper.SLASH;
import static dal.cs.quickcash3.util.StringHelper.splitString;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.regex.Pattern;

public class RegexSearchFilter<T> extends SearchFilter<T> {
    private final List<String> keys;
    private Pattern pattern;

    public RegexSearchFilter(@NonNull String key) {
        super();
        keys = splitString(key, SLASH);
    }

    public void setPattern(@NonNull Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean isCurrentValid(@NonNull JsonElement root) {
        if (pattern == null) {
            throw new NullPointerException("Cannot apply " + this + " without a pattern");
        }

        String value = getAt(root, keys).getAsString();
        return pattern.matcher(value).matches();
    }
}
