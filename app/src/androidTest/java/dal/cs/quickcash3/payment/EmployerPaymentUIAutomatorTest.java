package dal.cs.quickcash3.payment;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.Manifest;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class EmployerPaymentUIAutomatorTest {
    @Rule
    public final ActivityScenarioRule<EmployerPayPalActivity> activityRule =
            new ActivityScenarioRule<>(EmployerPayPalActivity.class);
    @Rule
    public final GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.CAMERA);
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());
    private static final int MAX_TIMEOUT = 30000;

    @Ignore("Inconsistently fails on CI")
    @Test
    public void checkValidCardPayment() throws UiObjectNotFoundException {
        UiObject paymentConfirmButton = device.findObject(new UiSelector().textContains("Confirm !"));
        assertTrue(paymentConfirmButton.exists());
        paymentConfirmButton.click();

        UiObject payCardButton = device.findObject(new UiSelector().textContains("Pay with Card"));
        assertTrue(payCardButton.waitForExists(MAX_TIMEOUT));
        payCardButton.click();

        UiObject keyboardButton = device.findObject(new UiSelector().textContains("Keyboard"));
        assertTrue(keyboardButton.waitForExists(MAX_TIMEOUT));
        keyboardButton.click();

        UiObject cardNumBox = device.findObject(new UiSelector().textContains("1234 5678"));
        assertTrue(cardNumBox.waitForExists(MAX_TIMEOUT));
        cardNumBox.setText("5317805091683572");
        UiObject expiresBox = device.findObject(new UiSelector().textContains("MM/YY"));
        expiresBox.setText("0825");
        UiObject cvvBox = device.findObject(new UiSelector().textContains("123"));
        cvvBox.setText("161");
        UiObject doneButton = device.findObject(new UiSelector().textContains("Done"));
        doneButton.click();

        UiObject chargeButton = device.findObject(new UiSelector().textContains("Charge Card"));
        assertTrue(chargeButton.waitForExists(MAX_TIMEOUT));
        chargeButton.click();

        UiObject approveStatus = device.findObject(new UiSelector().textContains("approved"));
        assertTrue(approveStatus.waitForExists(MAX_TIMEOUT));
    }

    // There is no way to give paypal an unapproved card.
}
