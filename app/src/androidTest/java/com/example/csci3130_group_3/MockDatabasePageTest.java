package com.example.csci3130_group_3;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MockDatabasePageTest {
    private Context context;
    private Database db;

    @Before
    public void setup() {
        db = new MockDatabase();
        ActivityScenario<DatabaseExampleActivity> scenario = ActivityScenario.launch(DatabaseExampleActivity.class);
        scenario.onActivity(activity -> {
            activity.setDatabase(db);
            context = activity;
        });
    }

    @Test
    public void write() {
        onView(withId(R.id.writeButton)).perform(click());
        onView(withId(R.id.dbOutput)).check(matches(withText(containsString(context.getString(R.string.db_write)))));
    }

    @Test
    public void read() {
        onView(withId(R.id.writeButton)).perform(click());
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
