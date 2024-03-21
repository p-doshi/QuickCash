package dal.cs.quickcash3.util;

import static junit.framework.Assert.assertFalse;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PromiseTest {

    @Test
    public void testIsReadyWhenNotFulfilled() {
        Promise<Integer> promise = new Promise<>();
        assertFalse(promise.isReady());
    }

    @Test
    public void testIsReadyAfterFulfill() {
        Promise<String> promise = new Promise<>();
        promise.fulfill("Test");
        assertTrue(promise.isReady());
    }

    @Test
    public void testRunWhenReadyImmediately() {
        Promise<Double> promise = new Promise<>();
        promise.fulfill(3.14);
        boolean[] executed = {false};
        promise.setUpdateCallback(item -> executed[0] = true);
        assertTrue(executed[0]);
    }

    @Test
    public void testRunWhenReadyLater() {
        Promise<Boolean> promise = new Promise<>();
        boolean[] executed = {false};
        promise.setUpdateCallback(item -> executed[0] = item);
        assertFalse(executed[0]);
        promise.fulfill(true);
        assertTrue(executed[0]);
    }
}
