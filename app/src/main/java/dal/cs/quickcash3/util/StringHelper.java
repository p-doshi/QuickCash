package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class StringHelper {
    public static final char SLASH = '/';

    // Utility class.
    private StringHelper() {}

    // TODO: create javadoc comment.
    public static boolean containsCharacter(@NonNull String str) {
        boolean foundCharacter = false;

        for(int i = 0; i < str.length(); i++) {
            if(!Character.isWhitespace(str.charAt(i))) {
                foundCharacter = true;
                break;
            }
        }

        return foundCharacter;
    }

    // TODO: create javadoc comment.
    public static @NonNull String getPluralEnding(int num) {
        return num == 1 ? "" : "s";
    }

    // TODO: create javadoc comment.
    public static @NonNull List<String> splitString(@NonNull String location, char delimiter) {
        List<String> strings = new ArrayList<>();

        String currentLocation = location;
        for (int slashPosition = currentLocation.indexOf(delimiter);
             slashPosition >= 0;
             slashPosition = currentLocation.indexOf(delimiter)) {

            String key = currentLocation.substring(0, slashPosition);
            currentLocation = currentLocation.substring(slashPosition + 1);

            if (!key.isEmpty()) {
                strings.add(key);
            }
        }

        if (!currentLocation.isEmpty()) {
            strings.add(currentLocation);
        }

        return strings;
    }
}
