package dal.cs.quickCash3.database;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Consumer;

public class MockDatabase implements Database {
    public static class MapType extends TreeMap<String, Object> {}
    private MapType data;

    public static List<String> splitLocationIntoKeys(String location) {
        ArrayList<String> strings = new ArrayList<>();

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

    private Object getData(String location) {
        List<String> keys = splitLocationIntoKeys(location);
        return recursiveGet(data, keys);
    }

    private Object recursiveGet(Object obj, @NonNull List<String> keys) {
        if (keys.isEmpty()) {
            return obj;
        }

        String key = keys.remove(0);

        if (!(obj instanceof MapType)) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        MapType map = (MapType)obj;

        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Key not found: " + key);
        }

        Object nestedData = map.get(key);
        return recursiveGet(nestedData, keys);
    }

    private <T> void setData(String location, T value) {
        List<String> keys = splitLocationIntoKeys(location);
        if (keys.isEmpty()) {
            throw new IllegalArgumentException("Must provide a location to write data");
        }
        if (data == null) {
            data = new MapType();
        }
        recursiveSet(data, keys, value);
    }

    private <T> void recursiveSet(MapType map, @NonNull List<String> keys, T value) {
        assert !keys.isEmpty();

        // Get the next key we are looking for.
        String key = keys.remove(0);

        // Is this the last key?
        if (keys.isEmpty()) {
            // TODO: serialize the type into a JSON object.
            map.put(key, value);
            return;
        }

        MapType nestedMap;
        Object nestedData = map.get(key);
        if (nestedData instanceof MapType) {
            nestedMap = (MapType)nestedData;
        }
        else {
            nestedMap = new MapType();
            map.put(key, nestedMap);
        }

        recursiveSet(nestedMap, keys, value);
    }

    @Override
    public <T> void write(String location, T value, Consumer<String> errorFunction) {
        write(location, value, () -> {}, errorFunction);
    }

    @Override
    public <T> void write(String location, T value, Runnable successFunction, Consumer<String> errorFunction) {
        try {
            setData(location, value);
            successFunction.run();
        }
        catch (Exception exception) {
            errorFunction.accept(exception.getMessage());
        }
    }

    @Override
    public <T> void read(String location, Class<T> type, Consumer<T> readFunction, Consumer<String> errorFunction) {
        try {
            Object obj = getData(location);
            T value = type.cast(obj);
            readFunction.accept(value);
        }
        catch (Exception exception) {
            errorFunction.accept(exception.getMessage());
        }
    }
}
