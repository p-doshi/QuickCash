package dal.cs.quickcash3.employer;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static dal.cs.quickcash3.test.ExampleJobList.generateAvailableJobs;
import static dal.cs.quickcash3.test.ExampleUserList.EMPLOYER1;
import static dal.cs.quickcash3.test.ExampleUserList.generateUsers;

import android.content.Context;
import android.content.Intent;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.mock.MockDatabase;
import dal.cs.quickcash3.geocode.MockGeocoder;
import dal.cs.quickcash3.payment.MockPayment;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") // This increases code readability.
@RunWith(AndroidJUnit4.class)
public class EmployerPaynowUITest {
    private final Context context = ApplicationProvider.getApplicationContext();
    @Rule
    public final ActivityScenarioRule<EmployerDashboard> activityRule =
        new ActivityScenarioRule<>(
            new Intent(context, EmployerDashboard.class)
                .addCategory(context.getString(R.string.MOCK_DATABASE))
                .addCategory(context.getString(R.string.MOCK_PAYMENT))
                .putExtra("user", EMPLOYER1));
    private final UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    private final String appPackage = context.getPackageName();
    private static final int MAX_TIMEOUT = 30000;
    private Database database;
    private MockPayment payment;

    private @NonNull UiObject scrollToText(@NonNull UiScrollable scrollable, @NonNull String text) throws UiObjectNotFoundException {
        UiSelector selector = new UiSelector().text(text);
        scrollable.scrollIntoView(selector);
        return device.findObject(selector);
    }

    private @NonNull UiSelector withResource(@NonNull String resourceId) {
        return new UiSelector().resourceId(appPackage + ":id/" + resourceId);
    }

    private @NonNull UiObject scrollToResource(@NonNull UiScrollable scrollable, @NonNull String resourceId) throws UiObjectNotFoundException {
        UiSelector selector = withResource(resourceId);
        scrollable.scrollIntoView(selector);
        return device.findObject(selector);
    }

    @Before
    public void setup() throws Throwable {
        ActivityScenario<EmployerDashboard> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Do not run the test if we are not using the mock implementation.
            assertTrue("Not using Mock Database",
                    activity.getDatabase() instanceof MockDatabase);
            assertTrue("Not using Mock Payment",
                    activity.getPayment() instanceof MockPayment);

            payment = (MockPayment) activity.getPayment();

            database = activity.getDatabase();
        });

        runOnUiThread(() -> generateAvailableJobs(database, Assert::fail));
        runOnUiThread(() -> generateUsers(database, Assert::fail));
    }

    @Test
    public void accept() throws UiObjectNotFoundException {
        UiScrollable jobListings = new UiScrollable(withResource("jobListRecyclerView"));
        scrollToText(jobListings, "Coding problem").click();

        UiScrollable jobDetails = new UiScrollable(new UiSelector().className(ScrollView.class));
        assertTrue(scrollToResource(jobDetails, "applicantsRecyclerView").exists());

        UiScrollable applicantsRecycler = new UiScrollable(withResource("applicantsRecyclerView"));
        UiObject applicant = applicantsRecycler.getChildByText(withResource("worker"), "Ethan Rozee");
        UiObject acceptButton = applicant.getFromParent(withResource("acceptButton"));
        acceptButton.click();

        payment.setSuccess("wertyjksvfrwgetegjhsfdg");
        UiObject confirmButton = device.findObject(new UiSelector().textContains("Confirm !"));
        assertTrue(confirmButton.waitForExists(MAX_TIMEOUT));
        confirmButton.click();

        UiObject payId = device.findObject(new UiSelector().textContains("Pay ID"));
        assertTrue(payId.waitForExists(MAX_TIMEOUT));

        // Make sure that the job is gone.
        device.pressBack();
        assertFalse(scrollToText(jobListings, "Coding problem").exists());
    }

    @Test
    public void doubleAccept() throws UiObjectNotFoundException {
        UiScrollable jobListings = new UiScrollable(withResource("jobListRecyclerView"));
        scrollToText(jobListings, "Coding problem").click();

        UiScrollable jobDetails = new UiScrollable(new UiSelector().className(ScrollView.class));
        assertTrue(scrollToResource(jobDetails, "applicantsRecyclerView").exists());

        UiScrollable applicantsRecycler = new UiScrollable(withResource("applicantsRecyclerView"));
        UiObject applicant = applicantsRecycler.getChildByText(withResource("worker"), "Ethan Rozee");
        UiObject acceptButton = applicant.getFromParent(withResource("acceptButton"));
        acceptButton.click();

        // User changed their mind.
        device.pressBack();

        UiObject applicant2 = applicantsRecycler.getChildByText(withResource("worker"), "Hayley Vezeau");
        UiObject acceptButton2 = applicant2.getFromParent(withResource("acceptButton"));
        acceptButton2.click();

        payment.setSuccess("wertyjksvfrwgetegjhsfdg");
        UiObject confirmButton = device.findObject(new UiSelector().textContains("Confirm !"));
        assertTrue(confirmButton.waitForExists(MAX_TIMEOUT));
        confirmButton.click();

        UiObject payId = device.findObject(new UiSelector().textContains("Pay ID"));
        assertTrue(payId.waitForExists(MAX_TIMEOUT));

        // Make sure that the job is gone.
        device.pressBack();
        assertFalse(scrollToText(jobListings, "Coding problem").exists());
    }

    @Test
    public void paymentFailure() throws UiObjectNotFoundException {
        UiScrollable jobListings = new UiScrollable(withResource("jobListRecyclerView"));
        scrollToText(jobListings, "Coding problem").click();

        UiScrollable jobDetails = new UiScrollable(new UiSelector().className(ScrollView.class));
        assertTrue(scrollToResource(jobDetails, "applicantsRecyclerView").exists());

        UiScrollable applicantsRecycler = new UiScrollable(withResource("applicantsRecyclerView"));
        UiObject applicant = applicantsRecycler.getChildByText(withResource("worker"), "Ethan Rozee");
        UiObject acceptButton = applicant.getFromParent(withResource("acceptButton"));
        acceptButton.click();

        payment.setFailure("Payment Declined");
        UiObject confirmButton = device.findObject(new UiSelector().textContains("Confirm !"));
        assertTrue(confirmButton.waitForExists(MAX_TIMEOUT));
        confirmButton.click();

        device.pressBack();
        assertTrue(applicant.waitForExists(MAX_TIMEOUT));
        UiObject applicant2 = applicantsRecycler.getChildByText(withResource("worker"), "Hayley Vezeau");
        assertTrue(applicant2.waitForExists(MAX_TIMEOUT));

        // Make sure that the job is still visible on listings.
        device.pressBack();
        assertTrue(scrollToText(jobListings, "Coding problem").exists());
    }
}
