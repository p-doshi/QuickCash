package dal.cs.quickcash3.login;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertNull;
import static dal.cs.quickcash3.test.WaitForAction.waitFor;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import dal.cs.quickcash3.R;

public class LoginEspressoTest {
    @Rule
    public final ActivityScenarioRule<LoginActivity> activityRule =
        new ActivityScenarioRule<>(LoginActivity.class);
    private static final long MAX_TIMEOUT = 30000;

    @After
    public void teardown() {
        // Just to be sure.
        assertNull(FirebaseAuth.getInstance().getCurrentUser());
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void testEmptyEmail() {
        onView(withId(R.id.etPassword)).perform(typeText("hi"), closeSoftKeyboard());
        onView(withId(R.id.continueButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_EMAIL_TOAST)));
    }

    @Test
    public void testEmptyPassword() {
        onView(withId(R.id.emailaddress)).perform(typeText("parthdoshi135@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.continueButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_PASSWORD_TOAST)));
    }

    @Test
    public void testInvalidEmail() {
        onView(withId(R.id.emailaddress)).perform(typeText("pdoshigmail.com"));
        onView(withId(R.id.etPassword)).perform(typeText("hahapranked"), closeSoftKeyboard());
        onView(withId(R.id.continueButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.INVALID_EMAIL_TOAST)));
    }

    @Test
    public void testInvalidCredentials() {
        onView(withId(R.id.emailaddress)).perform(typeText("parthdoshi135@gmail.com"));
        onView(withId(R.id.etPassword)).perform(typeText("hahapranked"), closeSoftKeyboard());
        onView(withId(R.id.continueButton)).perform(click());
        onView(withId(R.id.statusLabel)).perform(waitFor(withText(R.string.INVALID_CREDENTIALS), MAX_TIMEOUT));
    }
}
