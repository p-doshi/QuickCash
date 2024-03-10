package dal.cs.quickcash3.database;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.MyFirebaseDatabase;
import dal.cs.quickcash3.database.MyFirebaseDatabaseImpl;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private static final String RESOURCE_NAME = "databaseResource";
    private static final String TEST_DIR = "test";
    private static final String TEST_TEXT = "Hello";
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

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(TEST_DIR, TEST_TEXT,
            () -> {
                passed.set(true);
                resource.decrement();
            },
            newValue -> {
                error.set(newValue);
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
        final String randomValue = "aksdjdkjahsdiou123oiu124kjnoih1";
        AtomicReference<String> value = new AtomicReference<>(randomValue);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.read(TEST_DIR, String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            newValue -> {
                error.set(newValue);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertEquals(randomValue, value.get());
        Assert.assertEquals("Permission denied", error.get());

        registry.unregister(resource);
    }

    @Test
    public void writeSecureDatabaseSuccess() {
        Database database = new MyFirebaseDatabase(context);
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(TEST_DIR, TEST_TEXT,
            () -> {
                passed.set(true);
                resource.decrement();
            },
            newValue -> {
                error.set(newValue);
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
        final String randomValue = "aksdjdkjahsdiou123oiu124kjnoih1";
        AtomicReference<String> value = new AtomicReference<>(randomValue);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.read(TEST_DIR, String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            newValue -> {
                error.set(newValue);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        // We cannot guarantee what will be read from the database.
        // We just know we will receive _something_ or null.
        Assert.assertNotEquals(randomValue, value.get());
        Assert.assertNull(error.get());

        registry.unregister(resource);
    }

    @Test
    public void writeReadSecureDatabaseSuccess() {
        Database database = new MyFirebaseDatabase(context);
        AtomicReference<String> value = new AtomicReference<>(null);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        final String dir = "test";
        final String val = TEST_TEXT;
        database.write(
            dir,
            val,
            () -> database.read(dir, String.class,
                newValue -> {
                    value.set(newValue);
                    resource.decrement();
                },
                newValue -> {
                    error.set(newValue);
                    resource.decrement();
                }),
            newValue -> {
                error.set(newValue);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertEquals(val, value.get());
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

        database.write("public/test", TEST_TEXT,
            () -> {
                passed.set(true);
                resource.decrement();
            },
            newValue -> {
                error.set(newValue);
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
        final String randomValue = "aksdjdkjahsdiou123oiu124kjnoih1";
        AtomicReference<String> value = new AtomicReference<>(randomValue);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.read("public/test", String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            newValue -> {
                error.set(newValue);
                resource.decrement();
            });

        // Espresso will wait until our idle criterion is met.
        Espresso.onIdle();

        Assert.assertEquals(randomValue, value.get());
        Assert.assertEquals("Permission denied", error.get());

        registry.unregister(resource);
    }
}