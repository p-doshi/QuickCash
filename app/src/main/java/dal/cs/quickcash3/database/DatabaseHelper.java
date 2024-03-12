package dal.cs.quickcash3.database;

import java.util.ArrayList;
import java.util.List;

public final class DatabaseHelper {
    // Utility class.
    private DatabaseHelper() {}

    public static List<String> splitLocationIntoKeys(String location) {
        List<String> strings = new ArrayList<>();

        String currentLocation = location;
        for (int slashPosition = currentLocation.indexOf('/');
             slashPosition >= 0;
             slashPosition = currentLocation.indexOf('/')) {

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
