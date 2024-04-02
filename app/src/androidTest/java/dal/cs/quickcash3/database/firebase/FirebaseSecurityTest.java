package dal.cs.quickcash3.database.firebase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.test.CountingResourceRule;
import dal.cs.quickcash3.util.RandomStringGenerator;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") // This increases code readability.
@RunWith(AndroidJUnit4.class)
public class FirebaseSecurityTest {
    private static final String RANDOM_STRING = "aksdjdkjahsdiou123oiu124kjnoih1";
    @Rule
    public final CountingResourceRule resource = new CountingResourceRule("databaseResource");
    private String testDir;

    private @NonNull String getNewTestDir() {
        return "test/DatabaseTest/" + RandomStringGenerator.generate(10) + '/';
    }

    @Before
    public void setup() {
        testDir = getNewTestDir();
    }

    @Test
    public void writeDatabaseFailure() {
        Database database = new MyFirebaseDatabaseImpl();
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>(null);

        resource.increment();

        database.write(
            testDir,
            "Hello",
            () -> {
                passed.set(true);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        Espresso.onIdle();

        assertFalse(passed.get());
        assertEquals("Firebase Database error: Permission denied", error.get());
    }

    @Test
    public void readDatabaseFailure() {
        Database database = new MyFirebaseDatabaseImpl();

        // We need a value that shows that we have not received anything.
        AtomicReference<String> value = new AtomicReference<>(RANDOM_STRING);
        AtomicReference<String> error = new AtomicReference<>(null);

        resource.increment();

        database.read(
            testDir,
            String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        Espresso.onIdle();

        assertEquals(RANDOM_STRING, value.get());
        assertEquals("Permission denied", error.get());
    }

    @Test
    public void writeSecureDatabaseSuccess() {
        Database database = new MyFirebaseDatabase();
        AtomicBoolean passed = new AtomicBoolean(false);

        resource.increment();

        database.write(
            testDir,
            "Hello",
            () -> {
                passed.set(true);
                resource.decrement();
            },
            Assert::fail);

        Espresso.onIdle();

        assertTrue(passed.get());
    }

    @Test
    public void readSecureDatabaseSuccess() {
        Database database = new MyFirebaseDatabase();

        // We need a value that shows that we have not received anything.
        AtomicReference<String> value = new AtomicReference<>(RANDOM_STRING);

        resource.increment();

        database.read(
            testDir,
            String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            Assert::fail);

        Espresso.onIdle();

        // We cannot guarantee what will be read from the database.
        // We just know we will receive _something_ or null.
        assertNotEquals(RANDOM_STRING, value.get());
    }

    @Test
    public void writeReadSecureDatabaseSuccess() {
        Database database = new MyFirebaseDatabase();
        AtomicReference<String> value = new AtomicReference<>(null);

        resource.increment();

        database.write(
            testDir,
            "Hello",
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
}
