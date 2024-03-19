package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class StringHelper {
    public static final char SLASH = '/';

    // Utility class.
    private StringHelper() {}

    /**
     * Checks if the given string contains any non-whitespace character.
     *
     * @param str The string to check.
     * @return true if the string contains at least one non-whitespace character, false otherwise.
     */
    public static boolean isBlank(@NonNull String str) {
        boolean foundCharacter = false;

        for(int i = 0; i < str.length(); i++) {
            if(!Character.isWhitespace(str.charAt(i))) {
                foundCharacter = true;
                break;
            }
        }

        return foundCharacter;
    }

    /**
     * Returns the appropriate plural ending for a given count.
     *
     * @param num The count to evaluate.
     * @return An empty string if num is 1, otherwise "s".
     */
    public static @NonNull String getPluralEnding(int num) {
        return num == 1 ? "" : "s";
    }

    /**
     * Splits a string into a list of substrings based on a specified delimiter.
     * Substrings that are empty after the split are omitted from the result.
     *
     * @param location The string to be split.
     * @param delimiter The character used as the delimiter for splitting.
     * @return A list of substrings resulting from the split, excluding any empty strings.
     */
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

    /**
     * Splits a string into a list of substrings based on a specified delimiter.
     * Substrings that are empty after the split are omitted from the result.
     *
     * @param location The string to be split.
     * @param delimiter The character used as the delimiter for splitting.
     * @return A list of substrings resulting from the split, excluding any empty strings.
     */
    public static @NonNull String combineList(@NonNull List<String> keys, char delimiter) {
        StringBuilder output = new StringBuilder();

        boolean first = true;
        for (String key : keys) {
            if (!first) {
                output.append(delimiter);
            }
            first = false;
            output.append(key);
        }

        return output.toString();
    }
}
