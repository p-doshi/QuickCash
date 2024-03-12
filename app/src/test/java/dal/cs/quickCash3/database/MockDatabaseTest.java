package dal.cs.quickcash3.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") // Hard coded literals increase test code clarity.
public class MockDatabaseTest {
    private MockDatabase database;

    @Before
    public void setup() {
        database = new MockDatabase();
    }

    @Test
    public void writeWithoutSlash() {
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
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.read("test", String.class,
            value::set,
            error::set);

        assertNull(value.get());
        assertNotNull(error.get());
    }

    @Test
    public void writeThenRead() {
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

    @Test
    public void writeListen() {
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.write("a", "a", error::set);
        int listenerId = database.addListener("a", String.class, value::set, error::set);

        assertEquals(0, listenerId);
        assertEquals("a", value.get());
        assertNull(error.get());
    }

    @Test
    public void listenWrite() {
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        int listenerId = database.addListener("a", String.class, value::set, error::set);
        database.write("a", "a", error::set);

        assertEquals(0, listenerId);
        assertEquals("a", value.get());
        assertNotNull(error.get());
    }

    @Test
    public void writeListenWrite() {
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.write("a", "a", error::set);
        int listenerId = database.addListener("a", String.class, value::set, error::set);
        database.write("a", "b", error::set);

        assertEquals(0, listenerId);
        assertEquals("b", value.get());
        assertNull(error.get());
    }

    @Test
    public void writeListenError() {
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.write("a", "a", error::set);
        int listenerId = database.addListener("a", String.class, value::set, error::set);
        database.write("a/a", "b", error::set);

        assertEquals(0, listenerId);
        assertEquals("a", value.get());
        assertNotNull(error.get());
    }

    @Test
    public void writeListenErrorWrite() {
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.write("a", "a", error::set);
        int listenerId = database.addListener("a", String.class, value::set, error::set);
        database.write("a/a", "b", error::set);
        database.write("a", "c", error::set);

        assertEquals(0, listenerId);
        assertEquals("c", value.get());
        assertNotNull(error.get());
    }

    @Test
    public void writeListenWriteStopListenWrite() {
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.write("a", "a", error::set);
        int listenerId = database.addListener("a", String.class, value::set, error::set);
        database.write("a", "b", error::set);
        database.removeListener(listenerId);
        database.write("a", "c", error::set);

        assertEquals(0, listenerId);
        assertEquals("b", value.get());
        assertNull(error.get());
    }

    @Test
    public void delete() {
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>();

        database.delete("a",
            () -> passed.set(true),
            error::set);

        assertFalse(passed.get());
        assertNotNull(error.get());
    }

    @Test
    public void writeDelete() {
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>();

        database.write("a", "a", error::set);
        database.delete("a",
            () -> passed.set(true),
            error::set);

        assertTrue(passed.get());
        assertNull(error.get());
    }

    @Test
    public void writeDeleteRead() {
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.write("a", "a", error::set);
        database.delete("a", error::set);
        database.read("a", String.class, value::set, error::set);

        assertNull(value.get());
        assertNotNull(error.get());
    }

    @Test
    public void writeListenDelete() {
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.write("a", "a", error::set);
        int listenerId = database.addListener("a", String.class, value::set, error::set);
        database.delete("a", error::set);

        assertEquals(0, listenerId);
        assertEquals("a", value.get());
        assertNotNull(error.get());
    }

    @Test
    public void writeListenStopDelete() {
        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<String> error = new AtomicReference<>();

        database.write("a", "a", error::set);
        int listenerId = database.addListener("a", String.class, value::set, error::set);
        database.removeListener(listenerId);
        database.delete("a", error::set);

        assertEquals(0, listenerId);
        assertEquals("a", value.get());
        assertNull(error.get());
    }
}
