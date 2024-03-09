package dal.cs.quickCash3.RegistrationTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import dal.cs.quickCash3.registration.FormValidator;

public class FormValidatorUnitTest {
    private FormValidator form;

    @Before
    public void setUp() {
        form = new FormValidator();
    }

    @Test
    public void testFirstNameNotEmpty() {
        assertFalse(form.isFirstNameValid(""));
    }

    @Test
    public void testLastNameNotEmpty() {
        assertFalse(form.isLastNameValid(""));
    }

    @Test
    public void testAddressNotEmpty() {
        assertFalse(form.isAddressValid(""));
    }

    @Test
    public void testBirthDateNotEmpty() {
        Date date = null;
        assertFalse(form.isBirthDateValid(date));
    }

    @Test
    public void testUsernameNotEmpty() {
        assertFalse(form.isUserNameValid(""));
    }

    @Test
    public void testEmailFormat() {
        assertFalse(form.isEmailValid("invalidemail.com"));
        assertTrue(form.isEmailValid("valid@email.com"));
    }

    @Test
    public void testPasswordNotEmpty() {
        assertFalse(form.isPasswordValid(""));
    }

    @Test
    public void testConfirmPasswordMatches() {
        assertTrue(form.doPasswordsMatch("validpassword","validpassword"));

        assertFalse(form.doPasswordsMatch("validpassword","invalidpassword"));
    }
}
