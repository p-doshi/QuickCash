package com.example.csci3130_group_3;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;

public class MySignedFirebaseDatabase extends MyFirebaseDatabase {
    MySignedFirebaseDatabase(Activity activity) {
        super(initApp(activity));
    }

    private static Context initApp(Activity activity) {
        // Verify our app to use the database.
        FirebaseApp.initializeApp(activity);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();

        // Set the debug secret key in the shared preferences
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("debugSecret", "C5612FC4-B5A6-4A32-BBEB-10B274153782");
        editor.apply();

        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance());

        return activity;
    }
}
