package dal.cs.quickcash3.search;

import static dal.cs.quickcash3.util.GsonHelper.getAt;
import static dal.cs.quickcash3.util.StringHelper.SLASH;
import static dal.cs.quickcash3.util.StringHelper.splitString;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;

import java.util.List;

import dal.cs.quickcash3.util.Range;

public class NumericRangeSearchFilter<T> extends SearchFilter<T> {
    private final List<String> keys;
    private Range<Double> range = new Range<>(0.0, 0.0);

    public NumericRangeSearchFilter(@NonNull String key) {
        super();
        keys = splitString(key, SLASH);
    }

    public void setRange(@NonNull Range<Double> range) {
        this.range = range;
    }

    @Override
    public boolean isCurrentValid(@NonNull final JsonElement root) {
        double number = getAt(root, keys).getAsNumber().doubleValue();
        return range.contains(number);
    }
}
