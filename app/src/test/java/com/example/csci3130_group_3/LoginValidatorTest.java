package com.example.csci3130_group_3;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginValidatorTest {
    @Test
    public void checkValidEmail() {
        assertTrue(LoginValidator.isValidEmail("ethroz@dal.ca"));
    }

    @Test
    public void checkEmptyEmail() {
        assertTrue(LoginValidator.isEmptyEmail(""));
    }

    @Test
    public void checkEmptyPassword() {
        assertTrue(LoginValidator.isEmptyPassword(""));
    }

    @Test
    public void checkInvalidEmail() {
        assertFalse(LoginValidator.isValidEmail("ksdksdfn"));
    }
}