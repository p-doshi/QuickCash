package dal.cs.quickcash3.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.test.Person;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") // Hard coded literals increase test code clarity.
public class MockDatabaseTest {
    private static final List<Person> PEOPLE = Arrays.asList(
        new Person("aaa", "aaa", 1),
        new Person("aaa", "aab", 2),
        new Person("aab", "aaa", 3),
        new Person("aab", "aab", 4)
    );
    private MockDatabase database;

    private void writePeople() {
        for (int i = 0; i < PEOPLE.size(); i++) {
            database.write("a/" + i, PEOPLE.get(i), Assert::fail);
        }
    }

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

    private static <T> boolean containsInAnyOrder(@NonNull List<T> list1, @NonNull List<T> list2) {
        for (T v1: list1) {
            boolean found = false;
            for (T v2 : list2) {
                if (Objects.equals(v1, v2)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void directoryListen() {
        writePeople();

        List<Person> people = new ArrayList<>();

        int listenerId = database.addDirectoryListener(
            "a",
            Person.class,
            (key, person) -> people.add(person),
            Assert::fail);

        assertEquals(0, listenerId);
        assertTrue(containsInAnyOrder(people, PEOPLE));
    }

    @Test
    public void directoryListenWrite() {
        writePeople();

        List<Person> people = new ArrayList<>();

        int listenerId = database.addDirectoryListener(
            "a",
            Person.class,
            (key, person) -> people.add(person),
            Assert::fail);

        Person newPerson = new Person("aaa", "aaa", 5);
        database.write("a/4", newPerson, Assert::fail);

        List<Person> expectedPeople = new ArrayList<>(PEOPLE);
        expectedPeople.add(newPerson);

        assertEquals(0, listenerId);
        assertTrue(containsInAnyOrder(people, expectedPeople));
    }

    @Test
    public void directoryListenDelete() {
        writePeople();

        List<Person> people = new ArrayList<>();

        int listenerId = database.addDirectoryListener(
            "a",
            Person.class,
            (key, person) -> people.add(person),
            Assert::fail);

        database.delete("a/0", Assert::fail);

        List<Person> expectedPeople = new ArrayList<>(PEOPLE);
        expectedPeople.add(null);

        assertEquals(0, listenerId);
        assertTrue(containsInAnyOrder(people, expectedPeople));
    }
}
