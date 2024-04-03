package dal.cs.quickcash3.registration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dal.cs.quickcash3.R;

@SuppressWarnings("PMD.AvoidDuplicateLiterals") // I don't think it would increase code clarity here.
@RunWith(AndroidJUnit4.class)
public class RegistrationPageUITest {
    @Rule
    public final ActivityScenarioRule<RegistrationPage> activityRule =
        new ActivityScenarioRule<>(RegistrationPage.class);

    @Test
    public void fillRegistrationForm() {
        onView(withId(R.id.firstName)).perform(scrollTo(),typeText("John"));
        onView(withId(R.id.lastName)).perform(scrollTo(),typeText("Doe"));
        onView(withId(R.id.address)).perform(scrollTo(),typeText("123 Main Street"));
        onView(withId(R.id.birthYear)).perform(scrollTo(),typeText("1985"));
        onView(withId(R.id.birthMonth)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.birthDay)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.userName)).perform(scrollTo(),typeText("john_doe"));
        onView(withId(R.id.emailAddress)).perform(scrollTo(),typeText("john.doe@example.com"));
        onView(withId(R.id.password)).perform(scrollTo(),typeText("Password123"));
        onView(withId(R.id.confirmPassword)).perform(scrollTo(),typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(scrollTo(),click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.registrationStatus)).check(matches(withText(R.string.registration_successful)));
    }

    @Test
    public void checkInvalidFirstNameError(){
        onView(withId(R.id.lastName)).perform(scrollTo(),typeText("Doe"));
        onView(withId(R.id.address)).perform(scrollTo(),typeText("123 Main Street"));
        onView(withId(R.id.birthYear)).perform(scrollTo(),typeText("1985"));
        onView(withId(R.id.birthMonth)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.birthDay)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.userName)).perform(scrollTo(),typeText("john_doe"));
        onView(withId(R.id.emailAddress)).perform(scrollTo(),typeText("john.doe@example.com"));
        onView(withId(R.id.password)).perform(scrollTo(),typeText("Password123"));
        onView(withId(R.id.confirmPassword)).perform(scrollTo(),typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(scrollTo(),click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.registrationStatus)).check(matches(withText(R.string.invalid_first_name)));
    }

    @Test
    public void checkInvalidLastNameError(){
        onView(withId(R.id.firstName)).perform(scrollTo(),typeText("John"));
        onView(withId(R.id.address)).perform(scrollTo(),typeText("123 Main Street"));
        onView(withId(R.id.birthYear)).perform(scrollTo(),typeText("1985"));
        onView(withId(R.id.birthMonth)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.birthDay)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.userName)).perform(scrollTo(),typeText("john_doe"));
        onView(withId(R.id.emailAddress)).perform(scrollTo(),typeText("john.doe@example.com"));
        onView(withId(R.id.password)).perform(scrollTo(),typeText("Password123"));
        onView(withId(R.id.confirmPassword)).perform(scrollTo(),typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(scrollTo(),click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.registrationStatus)).check(matches(withText(R.string.invalid_last_name)));
    }

    @Test
    public void checkInvalidAddressError(){
        onView(withId(R.id.firstName)).perform(scrollTo(),typeText("John"));
        onView(withId(R.id.lastName)).perform(scrollTo(),typeText("Doe"));
        onView(withId(R.id.birthYear)).perform(scrollTo(),typeText("1985"));
        onView(withId(R.id.birthMonth)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.birthDay)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.userName)).perform(scrollTo(),typeText("john_doe"));
        onView(withId(R.id.emailAddress)).perform(scrollTo(),typeText("john.doe@example.com"));
        onView(withId(R.id.password)).perform(scrollTo(),typeText("Password123"));
        onView(withId(R.id.confirmPassword)).perform(scrollTo(),typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(scrollTo(),click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.registrationStatus)).check(matches(withText(R.string.invalid_address)));
    }

    @Test
    public void checkInvalidYearError() {
        onView(withId(R.id.firstName)).perform(scrollTo(),typeText("John"));
        onView(withId(R.id.lastName)).perform(scrollTo(),typeText("Doe"));
        onView(withId(R.id.address)).perform(scrollTo(),typeText("123 Main Street"));
        onView(withId(R.id.birthMonth)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.birthDay)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.userName)).perform(scrollTo(),typeText("john_doe"));
        onView(withId(R.id.emailAddress)).perform(scrollTo(),typeText("john.doe@example.com"));
        onView(withId(R.id.password)).perform(scrollTo(),typeText("Password123"));
        onView(withId(R.id.confirmPassword)).perform(scrollTo(),typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(scrollTo(),click());
        onView(withId(R.id.registrationStatus)).check(matches(withText(R.string.invalid_birth_date)));
    }

    @Test
    public void checkInvalidMonthError(){
        onView(withId(R.id.firstName)).perform(scrollTo(),typeText("John"));
        onView(withId(R.id.lastName)).perform(scrollTo(),typeText("Doe"));
        onView(withId(R.id.address)).perform(scrollTo(),typeText("123 Main Street"));
        onView(withId(R.id.birthYear)).perform(scrollTo(),typeText("1985"));
        onView(withId(R.id.birthDay)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.userName)).perform(scrollTo(),typeText("john_doe"));
        onView(withId(R.id.emailAddress)).perform(scrollTo(),typeText("john.doe@example.com"));
        onView(withId(R.id.password)).perform(scrollTo(),typeText("Password123"));
        onView(withId(R.id.confirmPassword)).perform(scrollTo(),typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(scrollTo(),click());
        onView(withId(R.id.registrationStatus)).check(matches(withText(R.string.invalid_birth_date)));
    }

    @Test
    public void checkInvalidDateError(){
        onView(withId(R.id.firstName)).perform(scrollTo(),typeText("John"));
        onView(withId(R.id.lastName)).perform(scrollTo(),typeText("Doe"));
        onView(withId(R.id.address)).perform(scrollTo(),typeText("123 Main Street"));
        onView(withId(R.id.birthYear)).perform(scrollTo(),typeText("1985"));
        onView(withId(R.id.birthMonth)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.userName)).perform(scrollTo(),typeText("john_doe"));
        onView(withId(R.id.emailAddress)).perform(scrollTo(),typeText("john.doe@example.com"));
        onView(withId(R.id.password)).perform(scrollTo(),typeText("Password123"));
        onView(withId(R.id.confirmPassword)).perform(scrollTo(),typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(scrollTo(),click());
        onView(withId(R.id.registrationStatus)).check(matches(withText(R.string.invalid_birth_date)));
    }

    @Test
    public void checkInvalidUserNameError(){
        onView(withId(R.id.firstName)).perform(scrollTo(),typeText("John"));
        onView(withId(R.id.lastName)).perform(scrollTo(),typeText("Doe"));
        onView(withId(R.id.address)).perform(scrollTo(),typeText("123 Main Street"));
        onView(withId(R.id.birthYear)).perform(scrollTo(),typeText("1985"));
        onView(withId(R.id.birthMonth)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.birthDay)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.emailAddress)).perform(scrollTo(),typeText("john.doe@example.com"));
        onView(withId(R.id.password)).perform(scrollTo(),typeText("Password123"));
        onView(withId(R.id.confirmPassword)).perform(scrollTo(),typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(scrollTo(),click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.registrationStatus)).check(matches(withText(R.string.invalid_user_name)));
    }

    @Test
    public void checkInvalidEmailError(){
        onView(withId(R.id.firstName)).perform(scrollTo(),typeText("John"));
        onView(withId(R.id.lastName)).perform(scrollTo(),typeText("Doe"));
        onView(withId(R.id.address)).perform(scrollTo(),typeText("123 Main Street"));
        onView(withId(R.id.birthYear)).perform(scrollTo(),typeText("1985"));
        onView(withId(R.id.birthMonth)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.birthDay)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.userName)).perform(scrollTo(),typeText("john_doe"));
        onView(withId(R.id.emailAddress)).perform(scrollTo(),typeText("john.doeexample.com"));
        onView(withId(R.id.password)).perform(scrollTo(),typeText("Password123"));
        onView(withId(R.id.confirmPassword)).perform(scrollTo(),typeText("Password123"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(scrollTo(),click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.registrationStatus)).check(matches(withText(R.string.invalid_email_address)));
    }

    @Test
    public void checkInvalidPassWordError(){
        onView(withId(R.id.firstName)).perform(scrollTo(),typeText("John"));
        onView(withId(R.id.lastName)).perform(scrollTo(),typeText("Doe"));
        onView(withId(R.id.address)).perform(scrollTo(),typeText("123 Main Street"));
        onView(withId(R.id.birthYear)).perform(scrollTo(),typeText("1985"));
        onView(withId(R.id.birthMonth)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.birthDay)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.userName)).perform(scrollTo(),typeText("john_doe"));
        onView(withId(R.id.emailAddress)).perform(scrollTo(),typeText("john.doe@example.com"));
        onView(withId(R.id.password)).perform(scrollTo(),typeText("xxx"));
        onView(withId(R.id.confirmPassword)).perform(scrollTo(),typeText("xxx"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(scrollTo(),click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.registrationStatus)).check(matches(withText(R.string.invalid_password)));
    }

    @Test
    public void checkInvalidPassWordConfirmError(){
        onView(withId(R.id.firstName)).perform(scrollTo(),typeText("John"));
        onView(withId(R.id.lastName)).perform(scrollTo(),typeText("Doe"));
        onView(withId(R.id.address)).perform(scrollTo(),typeText("123 Main Street"));
        onView(withId(R.id.birthYear)).perform(scrollTo(),typeText("1985"));
        onView(withId(R.id.birthMonth)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.birthDay)).perform(scrollTo(),typeText("01"));
        onView(withId(R.id.userName)).perform(scrollTo(),typeText("john_doe"));
        onView(withId(R.id.emailAddress)).perform(scrollTo(),typeText("john.doe@example.com"));
        onView(withId(R.id.password)).perform(scrollTo(),typeText("Password123"));
        onView(withId(R.id.confirmPassword)).perform(scrollTo(),typeText("xxx"),closeSoftKeyboard());

        onView(withId(R.id.confirmButton)).perform(scrollTo(),click());

        // Add more validation checks here after the button click if needed
        onView(withId(R.id.registrationStatus)).check(matches(withText(R.string.passwords_do_not_match)));
    }
}
