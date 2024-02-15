package com.example.csci3130_group_3;

import androidx.test.core.app.ActivityScenario;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertFalse;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)

public class RegistrationFormUITest {
    public ActivityScenario<RegistrationPage> scenario;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(RegistrationPage.class);
        scenario.onActivity(activity -> {
        });
    }

    @Test
    public void fillRegistrationForm() {

        onView(withId(R.id.firstName)).perform(typeText("John"),closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Doe"),closeSoftKeyboard());
        onView(withId(R.id.address)).perform(typeText("123 Main Street"),closeSoftKeyboard());
        onView(withId(R.id.birthYear)).perform(typeText("1985"),closeSoftKeyboard());
        onView(withId(R.id.birthMonth)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.birthDay)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.userName)).perform(typeText("john_doe"),closeSoftKeyboard());
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com"),closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Password123"),closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText("Registration successful!")));
    }

    @Test
    public void checkInvalidNameError(){
        onView(withId(R.id.firstName)).perform(typeText(""),closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Doe"),closeSoftKeyboard());
        onView(withId(R.id.address)).perform(typeText("123 Main Street"),closeSoftKeyboard());
        onView(withId(R.id.birthYear)).perform(typeText("1985"),closeSoftKeyboard());
        onView(withId(R.id.birthMonth)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.birthDay)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.userName)).perform(typeText("john_doe"),closeSoftKeyboard());
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com"),closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Password123"),closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid first name")));
    }

    @Test
    public void checkInvalidDateError(){
        onView(withId(R.id.firstName)).perform(typeText("John"),closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Doe"),closeSoftKeyboard());
        onView(withId(R.id.address)).perform(typeText("123 Main Street"),closeSoftKeyboard());
        onView(withId(R.id.birthYear)).perform(typeText(""),closeSoftKeyboard());
        onView(withId(R.id.birthMonth)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.birthDay)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.userName)).perform(typeText("john_doe"),closeSoftKeyboard());
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com"),closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Password123"),closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid birth date")));
    }

    @Test
    public void checkInvalidUserNameError(){
        onView(withId(R.id.firstName)).perform(typeText("John"),closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Doe"),closeSoftKeyboard());
        onView(withId(R.id.address)).perform(typeText("123 Main Street"),closeSoftKeyboard());
        onView(withId(R.id.birthYear)).perform(typeText("1985"),closeSoftKeyboard());
        onView(withId(R.id.birthMonth)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.birthDay)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.userName)).perform(typeText(""),closeSoftKeyboard());
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com"),closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Password123"),closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid user name")));
    }

    @Test
    public void checkInvalidEmailError(){
        onView(withId(R.id.firstName)).perform(typeText("John"),closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Doe"),closeSoftKeyboard());
        onView(withId(R.id.address)).perform(typeText("123 Main Street"),closeSoftKeyboard());
        onView(withId(R.id.birthYear)).perform(typeText("1985"),closeSoftKeyboard());
        onView(withId(R.id.birthMonth)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.birthDay)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.userName)).perform(typeText("john_doe"),closeSoftKeyboard());
        onView(withId(R.id.emailAddress)).perform(typeText("john.doeexample.com"),closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Password123"),closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid email address")));
    }

    @Test
    public void checkInvalidPassWordError(){
        onView(withId(R.id.firstName)).perform(typeText("John"),closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Doe"),closeSoftKeyboard());
        onView(withId(R.id.address)).perform(typeText("123 Main Street"),closeSoftKeyboard());
        onView(withId(R.id.birthYear)).perform(typeText("1985"),closeSoftKeyboard());
        onView(withId(R.id.birthMonth)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.birthDay)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.userName)).perform(typeText("john_doe"),closeSoftKeyboard());
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com"),closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("xxx"),closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("xxx"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText("Invalid password")));
    }

    @Test
    public void checkInvalidPassWordConfirmError(){
        onView(withId(R.id.firstName)).perform(typeText("John"),closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Doe"),closeSoftKeyboard());
        onView(withId(R.id.address)).perform(typeText("123 Main Street"),closeSoftKeyboard());
        onView(withId(R.id.birthYear)).perform(typeText("1985"),closeSoftKeyboard());
        onView(withId(R.id.birthMonth)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.birthDay)).perform(typeText("01"),closeSoftKeyboard());
        onView(withId(R.id.userName)).perform(typeText("john_doe"),closeSoftKeyboard());
        onView(withId(R.id.emailAddress)).perform(typeText("john.doe@example.com"),closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Password123"),closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("xxx"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.status)).check(ViewAssertions.matches(ViewMatchers.withText("Passwords do not match")));
    }
}
