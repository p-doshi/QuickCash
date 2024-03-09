package dal.cs.quickCash3.databaseAndroidTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import dal.cs.quickCash3.database.Database;
import dal.cs.quickCash3.database.MockDatabase;

public class MockDatabaseTest {
    @Test
    public void locationToKeysWithSlash() {
        List<String> expectedList = Arrays.asList("a", "b", "c", "d", "e");
        List<String> list = MockDatabase.splitLocationIntoKeys("/a/b/c/////d/e/");
        assertEquals(expectedList, list);
    }

    @Test
    public void locationToKeysWithoutSlash() {
        List<String> expectedList = Arrays.asList("a", "b", "c", "d", "e");
        List<String> list = MockDatabase.splitLocationIntoKeys("a/b/c/////d/e/");
        assertEquals(expectedList, list);
    }

    @Test
    public void writeWithoutSlash() {
        Database database = new MockDatabase();
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>();

        database.write("test", "Hello",
            () -> passed.set(true),
            error::set);

        assertTrue(passed.get());
        assertNull(error.get());
    }

    @Test
    public void writeWithSlash() {
        Database database = new MockDatabase();
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>();

        database.write("/test", "Hello",
            () -> passed.set(true),
            error::set);

        assertTrue(passed.get());
        assertNull(error.get());
    }

    @Test
    public void writeToRoot() {
        Database database = new MockDatabase();
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>();

        database.write("", "Hello",
            () -> passed.set(true),
            error::set);

        assertFalse(passed.get());
        assertNotNull(error.get());
    }

    @Test
    public void writeToSlash() {
        Database database = new MockDatabase();
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>();

        database.write("/", "Hello",
            () -> passed.set(true),
            error::set);

        assertFalse(passed.get());
        assertNotNull(error.get());
    }

    @Test
    public void readKey() {
        Database database = new MockDatabase();
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.read("test", String.class,
            value::set,
            error::set);

        assertNull(value.get());
        assertNotNull(error.get());
    }

    @Test
    public void readRoot() {
        Database database = new MockDatabase();
        AtomicReference<MockDatabase.MapType> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.read("", MockDatabase.MapType.class,
            value::set,
            error::set);

        assertNull(value.get());
        assertNull(error.get());
    }

    @Test
    public void writeThenRead() {
        Database database = new MockDatabase();
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        final String expectedValue = "Hello";
        database.write("test", expectedValue,
            () -> database.read("test", String.class,
                value::set,
                error::set),
            error::set);

        assertEquals(expectedValue, value.get());
        assertNull(error.get());
    }

    @Test
    public void writeSlashThenReadNoSlash() {
        Database database = new MockDatabase();
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        final String expectedValue = "Hello";
        database.write("/test", expectedValue,
            () -> database.read("test", String.class,
                value::set,
                error::set),
            error::set);

        assertEquals(expectedValue, value.get());
        assertNull(error.get());
    }

    @Test
    public void writeThenReadOther() {
        Database database = new MockDatabase();
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.write("test", "Hello",
            () -> database.read("tes", String.class,
                value::set,
                error::set),
            error::set);

        assertNull(value.get());
        assertNotNull(error.get());
    }

    @Test
    public void writeNestedThenReadRoot() {
        Database database = new MockDatabase();
        AtomicReference<MockDatabase.MapType> map = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        final String expectedValue = "Hello";
        final String expectedKey = "b";
        database.write("/a/" + expectedKey, expectedValue,
            () -> database.read("a", MockDatabase.MapType.class,
                map::set,
                error::set),
            error::set);

        assertEquals(expectedValue, map.get().get(expectedKey));
        assertNull(error.get());
    }

    @Test
    public void readOverwrittenNestedWrite() {
        Database database = new MockDatabase();
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        final String expectedValue = "Hello";
        database.write("a/b/c", "Bye", error::set);
        database.write("a/b", expectedValue,
            () -> database.read("a/b", String.class,
                value::set,
                error::set),
            error::set);

        assertEquals(expectedValue, value.get());
        assertNull(error.get());
    }

    @Test
    public void readNestedOverwrittenWrite() {
        Database database = new MockDatabase();
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        final String expectedValue = "Hello";
        database.write("a", "Bye", error::set);
        database.write("a/b", expectedValue,
            () -> database.read("a/b", String.class,
                value::set,
                error::set),
            error::set);

        assertEquals(expectedValue, value.get());
        assertNull(error.get());
    }

    @Test
    public void doubleWriteThenRead() {
        Database database = new MockDatabase();
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        final String expectedValue = "Hello";
        database.write("a", expectedValue, error::set);
        database.write("b", "Bye", error::set);
        database.read("a", String.class, value::set, error::set);

        assertEquals(expectedValue, value.get());
        assertNull(error.get());
    }

    @Test
    public void readInvalidType() {
        Database database = new MockDatabase();
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.write("a", 1,
            () -> database.read("a", String.class,
                value::set,
                error::set),
            error::set);

        assertNull(value.get());
        assertNotNull(error.get());
    }
}