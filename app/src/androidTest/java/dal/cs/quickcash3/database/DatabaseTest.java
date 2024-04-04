package dal.cs.quickcash3.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.AVAILABLE_JOBS;
import static dal.cs.quickcash3.test.ExampleUserList.EMPLOYERS;
import static dal.cs.quickcash3.test.ExampleUserList.WORKERS;

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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.CompletedJob;
import dal.cs.quickcash3.data.Employer;
import dal.cs.quickcash3.data.Worker;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.test.CountingResourceRule;
import dal.cs.quickcash3.test.Person;
import dal.cs.quickcash3.util.RandomStringGenerator;

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

    @Ignore("Manual Test Helper")
    @Test
    public void clearTestDir() {
        resource.increment();
        database.delete("test", resource::decrement, Assert::fail);
        Espresso.onIdle();
    }

    @Ignore("Manual data creation")
    @Test
    public void createDemoPublic() {
        // Delete.
        resource.increment();
        resource.increment();
        resource.increment();
        resource.increment();
        database.delete(AvailableJob.DIR, resource::decrement, Assert::fail);
        database.delete(CompletedJob.DIR, resource::decrement, Assert::fail);
        database.delete(Employer.DIR, resource::decrement, Assert::fail);
        database.delete(Worker.DIR, resource::decrement, Assert::fail);
        Espresso.onIdle();

        // Create Job Posts.
        AVAILABLE_JOBS.forEach(job -> {
            resource.increment();
            job.writeToDatabase(database, resource::decrement, Assert::fail);
        });

        // Create Users.
        EMPLOYERS.forEach(employer -> {
            resource.increment();
            employer.writeToDatabase(database, resource::decrement, Assert::fail);
        });

        WORKERS.forEach(worker -> {
            resource.increment();
            worker.writeToDatabase(database, resource::decrement, Assert::fail);
        });

        // Wait for them all to finish.
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
    public void directoryListener() {
        writePeople();

        List<Person> actualPeople = new ArrayList<>();

        List<Person> expectedPeople = PEOPLE;
        expectedPeople.forEach(unused -> resource.increment());

        int listenerId = database.addDirectoryListener(
            testDir,
            Person.class,
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
    public void directoryListenThenRead() {
        writePeople();

        List<String> keys = new ArrayList<>();
        List<Person> actualPeople = new ArrayList<>();

        List<Person> expectedPeople = PEOPLE;
        expectedPeople.forEach(unused -> resource.increment());

        int listenerId = database.addDirectoryListener(
            testDir,
            Person.class,
            (key, person) -> {
                keys.add(key);
                actualPeople.add(person);
                resource.decrement();
            },
            Assert::fail);

        Espresso.onIdle();

        database.removeListener(listenerId);

        assertThat(actualPeople, containsInAnyOrder(expectedPeople.toArray()));
        assertEquals(expectedPeople.size(), keys.size());

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
