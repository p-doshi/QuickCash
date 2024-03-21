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
    private static final String CONFIRM = "Confirm !";
    private static final String PAY_CARD = "Pay with Card";
    private static final String KEYBOARD = "Keyboard";
    private static final String CARD_BOX = "1234 5678";
    private static final String ACCEPT_CARD = "5317805091683572";
    private static final String DECLINED_CARD = "4002353549083015";
    private static final String EXP_BOX = "MM/YY";
    private static final String DATE = "0825";
    private static final String DECLINED_DATE = "0329";
    private static final String CVV_BOX = "123";
    private static final String CVV = "161";
    private static final String DECLINED_CVV = "123";
    private static final String DONE = "Done";
    private static final String CHARGE = "Charge Card";
    private static final String APPROVED = "approved";

    @Rule
    public final ActivityScenarioRule<EmployerPayPalActivity> activityRule =
            new ActivityScenarioRule<>(EmployerPayPalActivity.class);
    @Rule
    public final GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.CAMERA);
    private final UiDevice device = UiDevice.getInstance(getInstrumentation());

    @Ignore("Works locally but not CI yet")
    @Test
    public void checkValidCardPayment() throws UiObjectNotFoundException {
        UiObject paymentConfirmButton = device.findObject(new UiSelector().textContains(CONFIRM));
        paymentConfirmButton.clickAndWaitForNewWindow();
        UiObject payCardButton = device.findObject(new UiSelector().textContains(PAY_CARD));
        payCardButton.clickAndWaitForNewWindow();
        UiObject keyboardButton = device.findObject(new UiSelector().textContains(KEYBOARD));
        keyboardButton.clickAndWaitForNewWindow();
        UiObject cardNumBox = device.findObject(new UiSelector().textContains(CARD_BOX));
        cardNumBox.setText(ACCEPT_CARD);
        UiObject expiresBox = device.findObject(new UiSelector().textContains(EXP_BOX));
        expiresBox.setText(DATE);
        UiObject cvvBox = device.findObject(new UiSelector().textContains(CVV_BOX));
        cvvBox.setText(CVV);
        UiObject doneButton = device.findObject(new UiSelector().textContains(DONE));
        doneButton.clickAndWaitForNewWindow();
        UiObject chargeButton = device.findObject(new UiSelector().textContains(CHARGE));
        chargeButton.clickAndWaitForNewWindow();
        UiObject approveStatus = device.findObject(new UiSelector().textContains(APPROVED));
        assertTrue(approveStatus.waitForExists(10000));
    }

    @Ignore("Cannot find credit that doesn't work")
    @Test
    public void checkDeclinedCardPayment() throws UiObjectNotFoundException {
        UiObject paymentConfirmButton = device.findObject(new UiSelector().textContains(CONFIRM));
        paymentConfirmButton.clickAndWaitForNewWindow();
        UiObject payCardButton = device.findObject(new UiSelector().textContains(PAY_CARD));
        payCardButton.clickAndWaitForNewWindow();
        UiObject keyboardButton = device.findObject(new UiSelector().textContains(KEYBOARD));
        keyboardButton.clickAndWaitForNewWindow();
        UiObject cardNumBox = device.findObject(new UiSelector().textContains(CARD_BOX));
        cardNumBox.setText(DECLINED_CARD);
        UiObject expiresBox = device.findObject(new UiSelector().textContains(EXP_BOX));
        expiresBox.setText(DECLINED_DATE);
        UiObject cvvBox = device.findObject(new UiSelector().textContains(CVV_BOX));
        cvvBox.setText(DECLINED_CVV);
        UiObject doneButton = device.findObject(new UiSelector().textContains(DONE));
        doneButton.clickAndWaitForNewWindow();
        UiObject chargeButton = device.findObject(new UiSelector().textContains(CHARGE));
        chargeButton.clickAndWaitForNewWindow();
        UiObject approveStatus = device.findObject(new UiSelector().textContains(APPROVED));
        assertTrue(approveStatus.waitForExists(10000));
    }
}
