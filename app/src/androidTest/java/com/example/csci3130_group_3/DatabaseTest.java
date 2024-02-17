package com.example.csci3130_group_3;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    IdlingRegistry registry = IdlingRegistry.getInstance();
    Context context;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void writeDatabaseFailure() {
        Database db = new MyFirebaseDatabaseImpl(context);
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource("databaseResource");
        registry.register(resource);
        resource.increment();

        db.write("test", "Hello",
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
        Database db = new MyFirebaseDatabaseImpl(context);

        // We need a value that shows that we have not received anything.
        final String randomValue = "aksdjdkjahsdiou123oiu124kjnoih1";
        AtomicReference<String> value = new AtomicReference<>(randomValue);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource("databaseResource");
        registry.register(resource);
        resource.increment();

        db.read("test", String.class,
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
        Database db = new MyFirebaseDatabase(context);
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource("databaseResource");
        registry.register(resource);
        resource.increment();

        db.write("test", "Hello",
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
        Database db = new MyFirebaseDatabase(context);

        // We need a value that shows that we have not received anything.
        final String randomValue = "aksdjdkjahsdiou123oiu124kjnoih1";
        AtomicReference<String> value = new AtomicReference<>(randomValue);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource("databaseResource");
        registry.register(resource);
        resource.increment();

        db.read("test", String.class,
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
        Database db = new MyFirebaseDatabase(context);
        AtomicReference<String> value = new AtomicReference<>(null);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource("databaseResource");
        registry.register(resource);
        resource.increment();

        final String dir = "test";
        final String val = "Hello";
        db.write(
            dir,
            val,
            () -> db.read(dir, String.class,
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
        Database db = new MyFirebaseDatabase(context);
        AtomicBoolean passed = new AtomicBoolean(false);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource("databaseResource");
        registry.register(resource);
        resource.increment();

        db.write("public/test", "Hello",
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
        Database db = new MyFirebaseDatabase(context);

        // We need a value that shows that we have not received anything.
        final String randomValue = "aksdjdkjahsdiou123oiu124kjnoih1";
        AtomicReference<String> value = new AtomicReference<>(randomValue);
        AtomicReference<String> error = new AtomicReference<>(null);

        // Create and register the Idle Resource.
        CountingIdlingResource resource = new CountingIdlingResource("databaseResource");
        registry.register(resource);
        resource.increment();

        db.read("public/test", String.class,
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