package dal.cs.quickcash3.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class StringHelperTest {
    @Test
    public void locationToKeysWithSlash() {
        List<String> expectedList = Arrays.asList("a", "b", "c", "d", "e");
        List<String> list = StringHelper.splitString("/a/b/c/////d/e/", '/');
        assertEquals(expectedList, list);
    }

    @Test
    public void locationToKeysWithoutSlash() {
        List<String> expectedList = Arrays.asList("a", "b", "c", "d", "e");
        List<String> list = StringHelper.splitString("a/b/c/////d/e/", '/');
        assertEquals(expectedList, list);
    }
}
