package com.example.csci3130_group_3;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import java.util.function.Consumer;

/**
 * DO NOT use this class to interact with the database!
 * Please use MyFirebaseDatabase instead.
 */
public class MyFirebaseDatabaseImpl implements Database {
    private final FirebaseDatabase db;

    MyFirebaseDatabaseImpl(@NonNull Context context) {
        // Get a reference to the database.
        db = FirebaseDatabase.getInstance(context.getResources().getString(R.string.FIREBASE_DB_URL));
    }

    @Override
    public <T> void write(@NonNull String location, T value, @NonNull Consumer<String> errorFunction) {
        db.getReference(location).setValue(value)
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }

    @Override
    public <T> void write(@NonNull String location, T value, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        db.getReference(location).setValue(value)
            .addOnSuccessListener(unused -> successFunction.run())
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }


    @Override
    public <T> void read(@NonNull String location, @NonNull Class<T> type, @NonNull Consumer<T> readFunction, @NonNull Consumer<String> errorFunction) {
        db.getReference(location).get()
            .addOnSuccessListener(dataSnapshot -> {
                T value = dataSnapshot.getValue(type);
                readFunction.accept(value);
            })
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }
}
