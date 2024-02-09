package com.example.csci3130_group_3;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;

public class MyReleaseFirebaseDatabase extends MyFirebaseDatabase {
    MyReleaseFirebaseDatabase(Context context) {
        super(initApp(context));
    }

    private static Context initApp(Context context) {
        // Verify our app to use the database.
        FirebaseApp.initializeApp(context);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance());

        return context;
    }
}
