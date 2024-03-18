//package dal.cs.quickcash3.employer;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static androidx.test.espresso.action.ViewActions.typeText;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//
//import androidx.test.core.app.ActivityScenario;
//
//import dal.cs.quickCash3.R;
//import dal.cs.quickCash3.login.LoginActivity;
//import dal.cs.quickCash3.payment.EmployerPayPalActivity;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class EmployerPaymentEspressoTest {
//
//    public ActivityScenario<EmployerPayPalActivity> scenario;
//
//    @Before
//    public void setup() {
//        scenario = ActivityScenario.launch(EmployerPayPalActivity.class);
//        scenario.onActivity(activity -> {
//
//        });
//    }
//
//    @Test
//    public void testEmptyAmount() {
//        onView(withId(R.id.employerPayConfirmationButton)).perform(click());
//        onView(withId(R.id.transactionStatus)).check(matches(withText(R.string.INVALID_AMOUNT)));
//    }
//    @Test
//    public void testAlphaInvalidAmount() {
//        onView(withId(R.id.employerPayConfirmationButton)).perform(click());
//        onView(withId(R.id.transactionStatus)).check(matches(withText(R.string.INVALID_AMOUNT)));
//    }
//    @Test
//    public void testInvalidAmount() {
//        onView(withId(R.id.employerPayConfirmationButton)).perform(click());
//        onView(withId(R.id.transactionStatus)).check(matches(withText(R.string.INVALID_AMOUNT)));
//    }
//    @Test
//    public void testAcceptPayment() {
//        onView(withId(R.id.employerPayConfirmationButton)).perform(click());
//        onView(withId(R.id.PaymentStatusTitle)).check(matches(withText(R.string.PAYMENT_STATUS)));
//        onView(withId(R.id.transactionStatus)).check(matches(withText(R.string.approved)));
//    }
//}
//
//
