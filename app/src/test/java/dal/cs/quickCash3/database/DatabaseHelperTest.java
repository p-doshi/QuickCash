package dal.cs.quickcash3.database;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class DatabaseHelperTest {
    @Test
    public void locationToKeysWithSlash() {
        List<String> expectedList = Arrays.asList("a", "b", "c", "d", "e");
        List<String> list = DatabaseHelper.splitLocationIntoKeys("/a/b/c/////d/e/");
        assertEquals(expectedList, list);
    }

    @Test
    public void locationToKeysWithoutSlash() {
        List<String> expectedList = Arrays.asList("a", "b", "c", "d", "e");
        List<String> list = DatabaseHelper.splitLocationIntoKeys("a/b/c/////d/e/");
        assertEquals(expectedList, list);
    }
}
