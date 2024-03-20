package dal.cs.quickcash3.database;

import android.content.Context;
import android.util.Range;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.JobPostHelper;
import dal.cs.quickcash3.database.firebase.MyFirebaseDatabase;
import dal.cs.quickcash3.search.NumericRangeSearchFilter;
import dal.cs.quickcash3.search.RegexSearchFilter;
import dal.cs.quickcash3.test.Person;
import dal.cs.quickcash3.util.RandomStringGenerator;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") // This increases code readability.
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private final IdlingRegistry registry = IdlingRegistry.getInstance();
    private final Database database = new MyFirebaseDatabase();
    private CountingIdlingResource resource;
    private String testDir;

    private @NonNull String getNewTestDir() {
        return "test/DatabaseTest/" + RandomStringGenerator.generate(10) + '/';
    }

    @Before
    public void setup() {
        testDir = getNewTestDir();
        resource = new CountingIdlingResource("databaseResource");
        registry.register(resource);
    }

    @After
    public void teardown() {
        registry.unregister(resource);
    }

    @Test
    public void writeReadWrite() {
        AtomicReference<String> value = new AtomicReference<>(null);

        resource.increment();

        database.write(testDir, "Hello", Assert::fail);

        database.read(
            testDir,
            String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            Assert::fail);

        database.write(testDir, "Bye", Assert::fail);

        Espresso.onIdle();

        Assert.assertEquals("Hello", value.get());
    }

    @Ignore("Code to create some real jobs")
    @Test
    public void createJobs() {
        final int numJobs = 10;
        LatLng southeast = new LatLng(37.322998, -122.032181);
        LatLng northwest = new LatLng(37.354107, -121.955238);
        LatLngBounds area = new LatLngBounds(southeast, northwest);
        List<AvailableJob> jobs = JobPostHelper.generateAvailable(numJobs, area);
        Assert.assertEquals(numJobs, jobs.size());

        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());

        for (AvailableJob job : jobs) {
            resource.increment();
            job.writeToDatabase(database, resource::decrement, Assert::fail);
        }

        Espresso.onIdle();
    }

    @Test
    public void doubleWriteRead() {
        AtomicReference<String> value = new AtomicReference<>(null);

        resource.increment();

        String testDir2 = getNewTestDir();

        database.write(testDir, "Hello", Assert::fail);

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

        Assert.assertEquals("Hello", value.get());
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

        Assert.assertTrue(passed.get());
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

        Assert.assertNull(value.get());
    }

    private void writePeople() {
        Person[] people = {
            new Person("aaa", "aaa", 1),
            new Person("aaa", "aab", 2),
            new Person("aab", "aaa", 3),
            new Person("aab", "aab", 4)
        };

        for (Person person : people) {
            database.write(testDir + RandomStringGenerator.generate(10), person, Assert::fail);
        }
    }

    @Test
    public void searchOneFilter() {
        resource.increment();
        resource.increment();

        writePeople();

        List<Person> people = new ArrayList<>();

        RegexSearchFilter<Person> filter = new RegexSearchFilter<>("firstName");
        filter.setPattern(Pattern.compile("aaa"));

        int listenerId = database.addSearchListener(
            testDir,
            Person.class,
            filter,
            (key, person) -> {
                people.add(person);
                resource.decrement();
            },
            Assert::fail);

        Espresso.onIdle();

        database.removeListener(listenerId);

        Assert.assertEquals(2, people.size());
        for (Person person : people) {
            Assert.assertEquals("aaa", person.getFirstName());
        }
    }

    @Test
    public void searchTwoFilters() {
        resource.increment();
        resource.increment();

        writePeople();

        List<Person> people = new ArrayList<>();

        RegexSearchFilter<Person> filter1 = new RegexSearchFilter<>("firstName");
        filter1.setPattern(Pattern.compile(".*a.*"));
        NumericRangeSearchFilter<Person> filter2 = new NumericRangeSearchFilter<>("age");
        filter2.setRange(new Range<>(1.0, 2.0));
        filter1.addNext(filter2);

        int listenerId = database.addSearchListener(
            testDir,
            Person.class,
            filter1,
            (key, person) -> {
                people.add(person);
                resource.decrement();
            },
            Assert::fail);

        Espresso.onIdle();

        database.removeListener(listenerId);

        Assert.assertEquals(2, people.size());
        for (Person person : people) {
            Assert.assertEquals("aaa", person.getFirstName());
            Assert.assertTrue(person.getAge() >= 1.0);
            Assert.assertTrue(person.getAge() <= 2.0);
        }
    }

    @Test
    public void searchForOneThenRead() {
        resource.increment();

        writePeople();

        List<String> keys = new ArrayList<>();
        List<Person> people = new ArrayList<>();

        RegexSearchFilter<Person> filter1 = new RegexSearchFilter<>("firstName");
        filter1.setPattern(Pattern.compile("aaa"));
        RegexSearchFilter<Person> filter2 = new RegexSearchFilter<>("lastName");
        filter2.setPattern(Pattern.compile("aaa"));
        filter1.addNext(filter2);

        int listenerId = database.addSearchListener(
            testDir,
            Person.class,
            filter1,
            (key, person) -> {
                keys.add(key);
                people.add(person);
                resource.decrement();
            },
            Assert::fail);

        Espresso.onIdle();

        database.removeListener(listenerId);

        Assert.assertEquals(1, people.size());
        Assert.assertEquals(1, keys.size());

        String key = keys.get(0);
        Person expectedPerson = people.get(0);
        Assert.assertNotNull(key);
        Assert.assertNotNull(expectedPerson);

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

        Assert.assertEquals(expectedPerson, actualPerson.get());
    }
}
