package com.example.csci3130_group_3;

import androidx.test.core.app.ActivityScenario;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing\n">Testing documentation</a>
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals") // I don't think it would increase code clarity here.
@RunWith(AndroidJUnit4.class)

public class RegistrationPageUITest {
    public ActivityScenario<RegistrationPage> scenario;
    public Context context;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(RegistrationPage.class);
        scenario.onActivity(activity -> {
            context = activity;
        });
    }

    @Test
    public void fillRegistrationForm() {

        onView(withId(R.id.firstName)).perform(typeText("John\n"));
        onView(withId(R.id.lastName)).perform(typeText("Doe\n"));
        onView(withId(R.id.address)).perform(typeText("123 Main Street\n"));
        onView(withId(R.id.birthYear)).perform(typeText("1985\n"));
        onView(withId(R.id.birthMonth)).perform(typeText("01\n"));
        onView(withId(R.id.birthDay)).perform(typeText("01\n"));
        onView(withId(R.id.userName)).perform(typeText("john_doe\n"));
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com\n"));
        onView(withId(R.id.password)).perform(typeText("Password123\n"));
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123\n"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.registration_successful)));
    }

    @Test
    public void checkInvalidFirstNameError(){
        onView(withId(R.id.firstName)).perform(typeText("\n"));
        onView(withId(R.id.lastName)).perform(typeText("Doe\n"));
        onView(withId(R.id.address)).perform(typeText("123 Main Street\n"));
        onView(withId(R.id.birthYear)).perform(typeText("1985\n"));
        onView(withId(R.id.birthMonth)).perform(typeText("01\n"));
        onView(withId(R.id.birthDay)).perform(typeText("01\n"));
        onView(withId(R.id.userName)).perform(typeText("john_doe\n"));
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com\n"));
        onView(withId(R.id.password)).perform(typeText("Password123\n"));
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123\n"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.invalid_first_name)));
    }

    @Test
    public void checkInvalidLastNameError(){
        onView(withId(R.id.firstName)).perform(typeText("John\n"));
        onView(withId(R.id.lastName)).perform(typeText("\n"));
        onView(withId(R.id.address)).perform(typeText("123 Main Street\n"));
        onView(withId(R.id.birthYear)).perform(typeText("1985\n"));
        onView(withId(R.id.birthMonth)).perform(typeText("01\n"));
        onView(withId(R.id.birthDay)).perform(typeText("01\n"));
        onView(withId(R.id.userName)).perform(typeText("john_doe\n"));
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com\n"));
        onView(withId(R.id.password)).perform(typeText("Password123\n"));
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123\n"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.invalid_last_name)));
    }

    @Test
    public void checkInvalidAddressError(){
        onView(withId(R.id.firstName)).perform(typeText("John\n"));
        onView(withId(R.id.lastName)).perform(typeText("Doe\n"));
        onView(withId(R.id.address)).perform(typeText("\n"));
        onView(withId(R.id.birthYear)).perform(typeText("1985\n"));
        onView(withId(R.id.birthMonth)).perform(typeText("01\n"));
        onView(withId(R.id.birthDay)).perform(typeText("01\n"));
        onView(withId(R.id.userName)).perform(typeText("john_doe\n"));
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com\n"));
        onView(withId(R.id.password)).perform(typeText("Password123\n"));
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123\n"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.invalid_address)));
    }

    @Test
    public void checkInvalidYearError() {
        onView(withId(R.id.firstName)).perform(typeText("John\n"));
        onView(withId(R.id.lastName)).perform(typeText("Doe\n"));
        onView(withId(R.id.address)).perform(typeText("123 Main Street\n"));
        onView(withId(R.id.birthYear)).perform(typeText("\n"));
        onView(withId(R.id.birthMonth)).perform(typeText("01\n"));
        onView(withId(R.id.birthDay)).perform(typeText("01\n"));
        onView(withId(R.id.userName)).perform(typeText("john_doe\n"));
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com\n"));
        onView(withId(R.id.password)).perform(typeText("Password123\n"));
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123\n"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.invalid_birth_date)));
    }

    @Test
    public void checkInvalidMonthError(){
        onView(withId(R.id.firstName)).perform(typeText("John\n"));
        onView(withId(R.id.lastName)).perform(typeText("Doe\n"));
        onView(withId(R.id.address)).perform(typeText("123 Main Street\n"));
        onView(withId(R.id.birthYear)).perform(typeText("1985\n"));
        onView(withId(R.id.birthMonth)).perform(typeText("\n"));
        onView(withId(R.id.birthDay)).perform(typeText("01\n"));
        onView(withId(R.id.userName)).perform(typeText("john_doe\n"));
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com\n"));
        onView(withId(R.id.password)).perform(typeText("Password123\n"));
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123\n"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.invalid_birth_date)));
    }

    @Test
    public void checkInvalidDateError(){
        onView(withId(R.id.firstName)).perform(typeText("John\n"));
        onView(withId(R.id.lastName)).perform(typeText("Doe\n"));
        onView(withId(R.id.address)).perform(typeText("123 Main Street\n"));
        onView(withId(R.id.birthYear)).perform(typeText("1985\n"));
        onView(withId(R.id.birthMonth)).perform(typeText("01\n"));
        onView(withId(R.id.birthDay)).perform(typeText("\n"));
        onView(withId(R.id.userName)).perform(typeText("john_doe\n"));
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com\n"));
        onView(withId(R.id.password)).perform(typeText("Password123\n"));
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123\n"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.invalid_birth_date)));
    }

    @Test
    public void checkInvalidUserNameError(){
        onView(withId(R.id.firstName)).perform(typeText("John\n"));
        onView(withId(R.id.lastName)).perform(typeText("Doe\n"));
        onView(withId(R.id.address)).perform(typeText("123 Main Street\n"));
        onView(withId(R.id.birthYear)).perform(typeText("1985\n"));
        onView(withId(R.id.birthMonth)).perform(typeText("01\n"));
        onView(withId(R.id.birthDay)).perform(typeText("01\n"));
        onView(withId(R.id.userName)).perform(typeText("\n"));
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com\n"));
        onView(withId(R.id.password)).perform(typeText("Password123\n"));
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123\n"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.invalid_user_name)));
    }

    @Test
    public void checkInvalidEmailError(){
        onView(withId(R.id.firstName)).perform(typeText("John\n"));
        onView(withId(R.id.lastName)).perform(typeText("Doe\n"));
        onView(withId(R.id.address)).perform(typeText("123 Main Street\n"));
        onView(withId(R.id.birthYear)).perform(typeText("1985\n"));
        onView(withId(R.id.birthMonth)).perform(typeText("01\n"));
        onView(withId(R.id.birthDay)).perform(typeText("01\n"));
        onView(withId(R.id.userName)).perform(typeText("john_doe\n"));
        onView(withId(R.id.emailAddress)).perform(typeText("john.doeexample.com\n"));
        onView(withId(R.id.password)).perform(typeText("Password123\n"));
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123\n"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.invalid_email_address)));
    }

    @Test
    public void checkInvalidPassWordError(){
        onView(withId(R.id.firstName)).perform(typeText("John\n"));
        onView(withId(R.id.lastName)).perform(typeText("Doe\n"));
        onView(withId(R.id.address)).perform(typeText("123 Main Street\n"));
        onView(withId(R.id.birthYear)).perform(typeText("1985\n"));
        onView(withId(R.id.birthMonth)).perform(typeText("01\n"));
        onView(withId(R.id.birthDay)).perform(typeText("01\n"));
        onView(withId(R.id.userName)).perform(typeText("john_doe\n"));
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com\n"));
        onView(withId(R.id.password)).perform(typeText("xxx\n"));
        onView(withId(R.id.confirmPassword)).perform(typeText("xxx\n"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.invalid_password)));
    }

    @Test
    public void checkInvalidPassWordConfirmError(){
        onView(withId(R.id.firstName)).perform(typeText("John\n"));
        onView(withId(R.id.lastName)).perform(typeText("Doe\n"));
        onView(withId(R.id.address)).perform(typeText("123 Main Street\n"));
        onView(withId(R.id.birthYear)).perform(typeText("1985\n"));
        onView(withId(R.id.birthMonth)).perform(typeText("01\n"));
        onView(withId(R.id.birthDay)).perform(typeText("01\n"));
        onView(withId(R.id.userName)).perform(typeText("john_doe\n"));
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com\n"));
        onView(withId(R.id.password)).perform(typeText("Password123\n"));
        onView(withId(R.id.confirmPassword)).perform(typeText("xxx\n"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.passwords_do_not_match)));
    }
}
