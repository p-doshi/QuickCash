package com.example.csci3130_group_3;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MockDatabasePageTest {
    private Context context;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, DatabaseExampleActivity.class);
        intent.addCategory(context.getString(R.string.MOCK_DATABASE));
        ActivityScenario.launch(intent);
    }

    @Test
    public void write() {
        onView(withId(R.id.writeButton)).perform(click());
        onView(withId(R.id.dbOutput)).check(matches(withText(containsString(context.getString(R.string.db_write)))));
    }

    @Test
    public void read() {
        onView(withId(R.id.readButton)).perform(click());
        onView(withId(R.id.dbOutput)).check(matches(withText(containsString(context.getString(R.string.db_error_reading)))));
    }

    @Test
    public void writeRead() {
        onView(withId(R.id.writeButton)).perform(click());
        onView(withId(R.id.dbOutput)).check(matches(withText(containsString(context.getString(R.string.db_write)))));
        onView(withId(R.id.readButton)).perform(click());
        onView(withId(R.id.dbOutput)).check(matches(withText(containsString(context.getString(R.string.db_read)))));
    }
}
