package com.example.csci3130_group_3;

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

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private static final String RESOURCE_NAME = "databaseResource";
    private static final String TEST_DIR = "test/DatabaseTest";
    private static final String PUBLIC_DIR = "public/DatabaseTest";
    private static final String TEST_TEXT = "Hello";
    private static final String ALTERNATIVE_TEST_TEXT = "Bye";
    private static final String RANDOM_STRING = "aksdjdkjahsdiou123oiu124kjnoih1";
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

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.read(TEST_DIR, String.class,
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

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(TEST_DIR, TEST_TEXT,
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

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.read(TEST_DIR, String.class,
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

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(
            TEST_DIR,
            TEST_TEXT,
            () -> database.read(TEST_DIR, String.class,
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

        String dir1 = TEST_DIR + "/test1";
        String dir2 = TEST_DIR + "/test2";

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

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(
            TEST_DIR,
            TEST_TEXT,
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        database.read(TEST_DIR, String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        database.write(
            TEST_DIR,
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

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource(RESOURCE_NAME);
        registry.register(resource);
        resource.increment();

        database.write(
            TEST_DIR,
            ALTERNATIVE_TEST_TEXT,
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        int id = database.addListener(TEST_DIR, String.class,
            newValue -> {
                value.set(newValue);
                resource.decrement();
            },
            newError -> {
                error.set(newError);
                resource.decrement();
            });

        database.write(
            TEST_DIR,
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
}
