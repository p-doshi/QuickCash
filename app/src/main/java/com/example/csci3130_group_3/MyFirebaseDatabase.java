package com.example.csci3130_group_3;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.function.Consumer;

public class MyFirebaseDatabase implements Database {
    private final FirebaseDatabase db;

    MyFirebaseDatabase(Context context) {
        // Verify our app to use the database.
        FirebaseApp.initializeApp(context);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance());

        // Get a reference to the database.
        db = FirebaseDatabase.getInstance(context.getResources().getString(R.string.FIREBASE_DB_URL));
    }

    @Override
    public <T> void write(String location, T value) {
        db.getReference(location).setValue(value);
    }

    @Override
    public <T> void read(String location, Consumer<T> readFunction, Consumer<String> errorFunction) {
        db.getReference(location).get()
            .addOnSuccessListener(dataSnapshot -> {
                GenericTypeIndicator<T> t = new GenericTypeIndicator<T>() {};
                T value = dataSnapshot.getValue(t);
                readFunction.accept(value);
            })
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }
}
