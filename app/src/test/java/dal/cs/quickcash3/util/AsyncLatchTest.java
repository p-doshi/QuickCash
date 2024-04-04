package dal.cs.quickcash3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") // This increases code readability.
public class AsyncLatchTest {

    @Test
    public void testSetAndGet() {
        AsyncLatch<String> asyncLatch = new AsyncLatch<>();
        AtomicReference<String> result = new AtomicReference<>();

        asyncLatch.set("TestValue");
        asyncLatch.get(result::set);

        assertEquals("TestValue", result.get());
    }

    @Test
    public void testGetBeforeSet() {
        AsyncLatch<String> asyncLatch = new AsyncLatch<>();
        AtomicReference<String> result = new AtomicReference<>();

        asyncLatch.get(result::set);
        asyncLatch.set("TestValue");

        assertEquals("TestValue", result.get());
    }

    @Test
    public void testMultipleSetCalls() {
        AsyncLatch<String> asyncLatch = new AsyncLatch<>();
        AtomicReference<String> result = new AtomicReference<>();

        asyncLatch.set("FirstValue"); // This should have no effect
        asyncLatch.set("SecondValue");
        asyncLatch.get(result::set);

        assertEquals("SecondValue", result.get());
    }

    @Test
    public void testMultipleGetCalls() {
        AsyncLatch<String> asyncLatch = new AsyncLatch<>();
        AtomicReference<String> result1 = new AtomicReference<>();
        AtomicReference<String> result2 = new AtomicReference<>();

        asyncLatch.set("TestValue");
        asyncLatch.get(result1::set);
        asyncLatch.get(result2::set); // This should never be run

        assertEquals("TestValue", result1.get());
        assertNull(result2.get());
    }

    @Test
    public void testThreadSafety() throws InterruptedException {
        AsyncLatch<String> asyncLatch = new AsyncLatch<>();
        AtomicReference<String> result = new AtomicReference<>();

        Thread setterThread = new Thread(() -> asyncLatch.set("ThreadSafeValue"));
        Thread getterThread = new Thread(() -> asyncLatch.get(result::set));

        getterThread.start();
        setterThread.start();

        getterThread.join();
        setterThread.join();

        assertEquals("ThreadSafeValue", result.get());
    }
}
