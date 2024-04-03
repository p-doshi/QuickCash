package dal.cs.quickcash3.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.JOBS;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.search.NumericRangeSearchFilter;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.test.CountingResourceRule;
import dal.cs.quickcash3.test.Person;
import dal.cs.quickcash3.util.RandomStringGenerator;
import dal.cs.quickcash3.util.Range;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") // This increases code readability.
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private static final List<Person> PEOPLE = Arrays.asList(
        new Person("aaa", "aaa", 1),
        new Person("aaa", "aab", 2),
        new Person("aab", "aaa", 3),
        new Person("aab", "aab", 4)
    );
    @Rule
    public final CountingResourceRule resource = new CountingResourceRule("databaseResource");
    private final Database database = new MyFirebaseDatabase();
    private String testDir;

    private @NonNull String getNewTestDir() {
        return "test/DatabaseTest/" + RandomStringGenerator.generate(10) + '/';
    }

    private void writePeople() {
        PEOPLE.forEach(person -> {
            resource.increment();
            database.write(testDir + RandomStringGenerator.generate(10), person, resource::decrement, Assert::fail);
        });
        Espresso.onIdle();
    }

    @Before
    public void setup() {
        testDir = getNewTestDir();
    }

    @Test
    public void writeReadWrite() {
        AtomicReference<String> value = new AtomicReference<>(null);

        resource.increment();
        resource.increment();
        resource.increment();

        database.write(testDir, "Hello", resource::decrement, Assert::fail);

        database.read(
            testDir,
            String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            Assert::fail);

        database.write(testDir, "Bye", resource::decrement, Assert::fail);

        Espresso.onIdle();

        assertEquals("Hello", value.get());
    }

    @Ignore("Manual job creation")
    @Test
    public void createJobs() {
        for (Map.Entry<String, AvailableJob> entry : JOBS.entrySet()) {
            resource.increment();
            entry.getValue().writeToDatabase(database, entry.getKey(), resource::decrement, Assert::fail);
        }

        Espresso.onIdle();
    }

    @Test
    public void doubleWriteRead() {
        AtomicReference<String> value = new AtomicReference<>(null);

        resource.increment();
        resource.increment();

        String testDir2 = getNewTestDir();

        database.write(testDir, "Hello", resource::decrement, Assert::fail);

        database.write(
            testDir2,
            "Bye",
            () -> database.read(
                testDir,
                String.class,
                newValue -> {
                    value.set(newValue);
                    resource.decrement();
                },
                Assert::fail),
            Assert::fail);

        Espresso.onIdle();

        assertEquals("Hello", value.get());
    }

    @Test
    public void doubleWriteListen() {
        AtomicReference<String> value = new AtomicReference<>(null);

        resource.increment();
        resource.increment();
        resource.increment();
        resource.increment();

        database.write(testDir, "Bye", resource::decrement, Assert::fail);

        int listenerId = database.addListener(
            testDir,
            String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            Assert::fail);

        database.write(testDir, "Hello", resource::decrement, Assert::fail);

        Espresso.onIdle();

        database.removeListener(listenerId);

        assertEquals("Hello", value.get());
    }

    @Test
    public void writeDelete() {
        AtomicBoolean passed = new AtomicBoolean(false);

        resource.increment();

        database.write(
            testDir,
            "Hello",
            () -> database.delete(
                testDir,
                () -> {
                    passed.set(true);
                    resource.decrement();
                },
                Assert::fail),
            Assert::fail);

        Espresso.onIdle();

        assertTrue(passed.get());
    }

    @Test
    public void writeDeleteRead() {
        AtomicReference<String> value = new AtomicReference<>(null);

        resource.increment();

        database.write(
            testDir,
            "Hello",
            () -> database.delete(
                testDir,
                () -> database.read(
                    testDir,
                    String.class,
                    newValue -> {
                        value.set(newValue);
                        resource.decrement();
                    },
                    Assert::fail),
                Assert::fail),
            Assert::fail);

        Espresso.onIdle();

        assertNull(value.get());
    }

    @Test
    public void searchOneFilter() {
        writePeople();

        List<Person> actualPeople = new ArrayList<>();

        RegexSearchFilter<Person> filter = new RegexSearchFilter<>("firstName");
        filter.setPattern(Pattern.compile("aaa"));

        List<Person> expectedPeople = PEOPLE.subList(0, 2);
        expectedPeople.forEach(unused -> resource.increment());

        int listenerId = database.addSearchListener(
            testDir,
            Person.class,
            filter,
            (key, person) -> {
                actualPeople.add(person);
                resource.decrement();
            },
            Assert::fail);

        Espresso.onIdle();

        database.removeListener(listenerId);

        assertThat(actualPeople, containsInAnyOrder(expectedPeople.toArray()));
    }

    @Test
    public void searchTwoFilters() {
        writePeople();

        List<Person> actualPeople = new ArrayList<>();

        RegexSearchFilter<Person> filter1 = new RegexSearchFilter<>("firstName");
        filter1.setPattern(Pattern.compile(".*a.*"));
        NumericRangeSearchFilter<Person> filter2 = new NumericRangeSearchFilter<>("age");
        filter2.setRange(new Range<>(2.0, 3.0));
        filter1.addNext(filter2);

        List<Person> expectedPeople = PEOPLE.subList(1, 1);
        expectedPeople.forEach(unused -> resource.increment());

        int listenerId = database.addSearchListener(
            testDir,
            Person.class,
            filter1,
            (key, person) -> {
                actualPeople.add(person);
                resource.decrement();
            },
            Assert::fail);

        Espresso.onIdle();

        database.removeListener(listenerId);

        assertThat(actualPeople, containsInAnyOrder(expectedPeople.toArray()));
    }

    @Test
    public void searchForOneThenRead() {
        writePeople();

        List<String> keys = new ArrayList<>();
        List<Person> actualPeople = new ArrayList<>();

        RegexSearchFilter<Person> filter1 = new RegexSearchFilter<>("firstName");
        filter1.setPattern(Pattern.compile("aaa"));
        RegexSearchFilter<Person> filter2 = new RegexSearchFilter<>("lastName");
        filter2.setPattern(Pattern.compile("aaa"));
        filter1.addNext(filter2);

        List<Person> expectedPeople = PEOPLE.subList(0, 1);
        expectedPeople.forEach(unused -> resource.increment());

        int listenerId = database.addSearchListener(
            testDir,
            Person.class,
            filter1,
            (key, person) -> {
                keys.add(key);
                actualPeople.add(person);
                resource.decrement();
            },
            Assert::fail);

        Espresso.onIdle();

        database.removeListener(listenerId);

        assertThat(actualPeople, containsInAnyOrder(expectedPeople.toArray()));
        assertEquals(1, keys.size());

        String key = keys.get(0);
        Person expectedPerson = actualPeople.get(0);
        assertNotNull(key);
        assertNotNull(expectedPerson);

        resource.increment();

        AtomicReference<Person> actualPerson = new AtomicReference<>();
        database.read(
            testDir + key,
            Person.class,
            person -> {
                actualPerson.set(person);
                resource.decrement();
            },
            Assert::fail);

        Espresso.onIdle();

        assertEquals(expectedPerson, actualPerson.get());
    }
}
