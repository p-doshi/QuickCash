package dal.cs.quickCash3.databaseAndroidTests;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import dal.cs.quickCash3.database.Database;
import dal.cs.quickCash3.database.MyFirebaseDatabase;
import dal.cs.quickCash3.database.MyFirebaseDatabaseImpl;
import dal.cs.quickCash3.util.RandomStringGenerator;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private static final String RESOURCE_NAME = "databaseResource";
    private static final String BASE_TEST_DIR = "test/DatabaseTest/";
    private static final String PUBLIC_DIR = "public/DatabaseTest";
    private static final String TEST_TEXT = "Hello";
    private static final String ALTERNATIVE_TEST_TEXT = "Bye";
    private static final String RANDOM_STRING = "aksdjdkjahsdiou123oiu124kjnoih1";
    private static final int RANDOM_LENGTH = 10;
    private final IdlingRegistry registry = IdlingRegistry.getInstance();
    private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Before
    public void setup() {
        // Make sure we are starting in a signed out state.
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void writeDatabaseFailure() {
        Database database = new MyFirebaseDatabaseImpl(context);
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>(null);
        String testDir = BASE_TEST_DIR + RandomStringGenerator.generate(RANDOM_LENGTH);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(testDir, TEST_TEXT,
            () -> {
                passed.set(true);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertFalse(passed.get());
        Assert.assertEquals("Firebase Database error: Permission denied", error.get());

        registry.unregister(resource);
    }

    @Test
    public void readDatabaseFailure() {
        Database database = new MyFirebaseDatabaseImpl(context);

        // We need a value that shows that we have not received anything.
        AtomicReference<String> value = new AtomicReference<>(RANDOM_STRING);
        AtomicReference<String> error = new AtomicReference<>(null);
        String testDir = BASE_TEST_DIR + RandomStringGenerator.generate(RANDOM_LENGTH);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.read(testDir, String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertEquals(RANDOM_STRING, value.get());
        Assert.assertEquals("Permission denied", error.get());

        registry.unregister(resource);
    }

    @Test
    public void writeSecureDatabaseSuccess() {
        Database database = new MyFirebaseDatabase(context);
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>(null);
        String testDir = BASE_TEST_DIR + RandomStringGenerator.generate(RANDOM_LENGTH);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(testDir, TEST_TEXT,
            () -> {
                passed.set(true);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertTrue(passed.get());
        Assert.assertNull(error.get());

        registry.unregister(resource);
    }

    @Test
    public void readSecureDatabaseSuccess() {
        Database database = new MyFirebaseDatabase(context);

        // We need a value that shows that we have not received anything.
        AtomicReference<String> value = new AtomicReference<>(RANDOM_STRING);
        AtomicReference<String> error = new AtomicReference<>(null);
        String testDir = BASE_TEST_DIR + RandomStringGenerator.generate(RANDOM_LENGTH);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.read(testDir, String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        // We cannot guarantee what will be read from the database.
        // We just know we will receive _something_ or null.
        Assert.assertNotEquals(RANDOM_STRING, value.get());
        Assert.assertNull(error.get());

        registry.unregister(resource);
    }

    @Test
    public void writeReadSecureDatabaseSuccess() {
        Database database = new MyFirebaseDatabase(context);
        AtomicReference<String> value = new AtomicReference<>(null);
        AtomicReference<String> error = new AtomicReference<>(null);
        String testDir = BASE_TEST_DIR + RandomStringGenerator.generate(RANDOM_LENGTH);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(
            testDir,
            TEST_TEXT,
            () -> database.read(testDir, String.class,
                newValue -> {
                    value.set(newValue);
                    resource.decrement();
                },
                newError -> {
                    error.set(newError);
                    resource.decrement();
                }),
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertEquals(TEST_TEXT, value.get());
        Assert.assertNull(error.get());

        registry.unregister(resource);
    }

    @Test
    public void writeSecureAuthorizedDatabaseFailure() {
        Database database = new MyFirebaseDatabase(context);
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(PUBLIC_DIR, TEST_TEXT,
            () -> {
                passed.set(true);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertFalse(passed.get());
        Assert.assertEquals("Firebase Database error: Permission denied", error.get());

        registry.unregister(resource);
    }

    @Test
    public void readSecureAuthorizedDatabaseFailure() {
        Database database = new MyFirebaseDatabase(context);

        // We need a value that shows that we have not received anything.
        AtomicReference<String> value = new AtomicReference<>(RANDOM_STRING);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.read(PUBLIC_DIR, String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertEquals(RANDOM_STRING, value.get());
        Assert.assertEquals("Permission denied", error.get());

        registry.unregister(resource);
    }

    @Test
    public void doubleWriteRead() {
        Database database = new MyFirebaseDatabase(context);
        AtomicReference<String> value = new AtomicReference<>(null);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        String dir1 = BASE_TEST_DIR + RandomStringGenerator.generate(RANDOM_LENGTH);
        String dir2 = BASE_TEST_DIR + RandomStringGenerator.generate(RANDOM_LENGTH);

        database.write(
            dir1,
            TEST_TEXT,
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // This write should not overwrite the first one.
        database.write(
            dir2,
            ALTERNATIVE_TEST_TEXT,
            () -> database.read(dir1, String.class,
                newValue -> {
                    value.set(newValue);
                    resource.decrement();
                },
                newError -> {
                    error.set(newError);
                    resource.decrement();
                }),
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertEquals(TEST_TEXT, value.get());
        Assert.assertNull(error.get());

        registry.unregister(resource);
    }

    @Test
    public void writeReadWrite() {
        Database database = new MyFirebaseDatabase(context);
        AtomicReference<String> value = new AtomicReference<>(null);
        AtomicReference<String> error = new AtomicReference<>(null);
        String testDir = BASE_TEST_DIR + RandomStringGenerator.generate(RANDOM_LENGTH);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(
            testDir,
            TEST_TEXT,
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        database.read(testDir, String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        database.write(
            testDir,
            ALTERNATIVE_TEST_TEXT,
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertEquals(TEST_TEXT, value.get());
        Assert.assertNull(error.get());

        registry.unregister(resource);
    }

    @Test
    public void doubleWriteListen() {
        Database database = new MyFirebaseDatabase(context);
        AtomicReference<String> value = new AtomicReference<>(null);
        AtomicReference<String> error = new AtomicReference<>(null);
        String testDir = BASE_TEST_DIR + RandomStringGenerator.generate(RANDOM_LENGTH);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();
        resource.increment();

        database.write(
            testDir,
            ALTERNATIVE_TEST_TEXT,
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        int id = database.addListener(testDir, String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        database.write(
            testDir,
            TEST_TEXT,
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        database.removeListener(id);

        Assert.assertEquals(TEST_TEXT, value.get());
        Assert.assertNull(error.get());

        registry.unregister(resource);
    }

    @Test
    public void writeDelete() {
        Database database = new MyFirebaseDatabase(context);
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>(null);
        String testDir = BASE_TEST_DIR + RandomStringGenerator.generate(RANDOM_LENGTH);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(
            testDir,
            TEST_TEXT,
            () -> database.delete(testDir,
                () -> {
                    passed.set(true);
                    resource.decrement();
                },
                newError -> {
                    error.set(newError);
                    resource.decrement();
                }),
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertTrue(passed.get());
        Assert.assertNull(error.get());

        registry.unregister(resource);
    }

    @Test
    public void writeDeleteRead() {
        Database database = new MyFirebaseDatabase(context);
        AtomicReference<String> value = new AtomicReference<>(null);
        AtomicReference<String> error = new AtomicReference<>(null);
        String testDir = BASE_TEST_DIR + RandomStringGenerator.generate(RANDOM_LENGTH);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(testDir, TEST_TEXT,
            () -> database.delete(testDir,
                () -> database.read(testDir, String.class,
                    newValue -> {
                        value.set(newValue);
                        resource.decrement();
                    },
                    newError -> {
                        error.set(newError);
                        resource.decrement();
                    }),
                newError -> {
                    error.set(newError);
                    resource.decrement();
                }),
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertNull(value.get());
        Assert.assertNull(error.get());

        registry.unregister(resource);
    }
}
