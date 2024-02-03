package com.example.csci3130_group_3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginValidatorUnitTest {
    @Test
    public void checkEmailNull() {
        assertFalse(LoginValidator.isValidEmail(null));
    }

}