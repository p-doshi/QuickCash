package dal.cs.quickcash3.search;

import static dal.cs.quickcash3.util.GsonHelper.getAt;
import static dal.cs.quickcash3.util.StringHelper.SLASH;
import static dal.cs.quickcash3.util.StringHelper.splitString;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;

import java.util.List;

public class ListSearchFilter<T> extends SearchFilter<T> {
    private final List<String> keys;
    private List<String> list;

    public ListSearchFilter(@NonNull String key) {
        super();
        keys = splitString(key, SLASH);
    }

    public void setList(@NonNull List<String> list) {
        this.list = list;
    }

    @Override
    public boolean isCurrentValid(@NonNull JsonElement root) {
        if (list == null) {
            throw new NullPointerException("Cannot apply " + this + " without a list");
        }

        String value = getAt(root, keys).getAsString();
        return list.contains(value);
    }
}
