package com.example.csci3130_group_3;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class LoginUIAutomatorTest {
    private static final int LAUNCH_TIMEOUT = 10000;
    final String launcherPackage = "com.example.csci3130_group_3";
    private UiDevice device;
    public Context context;
    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());
        this.context = ApplicationProvider.getApplicationContext();
        launchApp();
    }

    public void launchApp(){
        final Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackage);
        assert appIntent != null;
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(appIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
    }
    @Test
    public void checkIfLandingPageIsVisible() {
        UiObject emailIDBox = device.findObject(new UiSelector().textContains("Email"));
        assertTrue(emailIDBox.exists());
        UiObject roleSpinner = device.findObject(new UiSelector().textContains("Password"));
        assertTrue(roleSpinner.exists());
        UiObject registerButton = device.findObject(new UiSelector().text("Continue"));
        assertTrue(registerButton.exists());
    }


    //below tests wont work until pages connected :(
    @Test
    @Ignore("only after pages connected")
    public void checkIfMovedToDashboard() throws UiObjectNotFoundException {

        UiObject emailIDBox = device.findObject(new UiSelector().textContains("Email"));
        emailIDBox.setText("parthdoshi135@gmail.com");
        UiObject passwordBox = device.findObject(new UiSelector().textContains("Password"));
        passwordBox.setText("Password");
        UiObject registerButton = device.findObject(new UiSelector().text("Continue"));
        registerButton.clickAndWaitForNewWindow();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains("Welcome"));
        assertTrue(welcomeLabel.exists());
    }

    @Test
    @Ignore("only after pages connected")
    public void checkIfRememberMeWorks() throws UiObjectNotFoundException, IOException {
        UiObject emailIDBox = device.findObject(new UiSelector().textContains("Email"));
        emailIDBox.setText("parthdoshi135@gmail.com");
        UiObject passwordBox = device.findObject(new UiSelector().textContains("Password"));
        passwordBox.setText("Password");
        UiObject checkbox = device.findObject(new UiSelector().textContains("Remember"));
        checkbox.click();
        UiObject registerButton = device.findObject(new UiSelector().text("Continue"));
        registerButton.click();
        Runtime.getRuntime().exec(new String[] {"am", "force-stop", "com.example.csci3130_group_3"});
        launchApp();
    }
    @Test
    @Ignore("only after pages connected")
    public void checkIfMovedToSignUpPage() throws UiObjectNotFoundException {

        UiObject emailIDBox = device.findObject(new UiSelector().textContains("Email"));
        emailIDBox.setText("parthdoshi135@gmail.com");
        UiObject passwordBox = device.findObject(new UiSelector().textContains("Password"));
        passwordBox.setText("Password");
        UiObject registerButton = device.findObject(new UiSelector().text("Continue"));
        registerButton.clickAndWaitForNewWindow();
        UiObject welcomeLabel = device.findObject(new UiSelector().textContains("Welcome"));
        assertTrue(welcomeLabel.exists());
    }

}
