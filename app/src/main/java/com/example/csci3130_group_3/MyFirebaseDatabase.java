package com.example.csci3130_group_3;

import android.content.Context;
import com.google.firebase.database.FirebaseDatabase;
import java.util.function.Consumer;

public class MyFirebaseDatabase implements Database {
    private final FirebaseDatabase db;

    MyFirebaseDatabase(Context context) {
        // Get a reference to the database.
        db = FirebaseDatabase.getInstance(context.getResources().getString(R.string.FIREBASE_DB_URL));
    }

    @Override
    public <T> void write(String location, T value, Consumer<String> errorFunction) {
        db.getReference(location).setValue(value)
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }

    @Override
    public <T> void write(String location, T value, Runnable successFunction, Consumer<String> errorFunction) {
        db.getReference(location).setValue(value)
            .addOnSuccessListener(unused -> successFunction.run())
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }


    @Override
    public <T> void read(String location, Class<T> type, Consumer<T> readFunction, Consumer<String> errorFunction) {
        db.getReference(location).get()
            .addOnSuccessListener(dataSnapshot -> {
                T value = dataSnapshot.getValue(type);
                readFunction.accept(value);
            })
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }
}
