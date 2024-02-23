package com.example.csci3130_group_3;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public class MyFirebaseDatabase extends MyFirebaseDatabaseImpl {
    private static final String DB_KEY = "nP5exoTNYnlqpPD1B3BHeuNDcWaPxI";

    MyFirebaseDatabase(@NonNull Context context) {
        super(context);
    }

    protected @NonNull String relocate(@NonNull String location) {
        StringBuilder newLocation = new StringBuilder("/" + DB_KEY);

        if (!location.startsWith("/")) {
            newLocation.append('/');
        }
        newLocation.append(location);

        return newLocation.toString();
    }

    @Override
    public <T> void read(@NonNull String location, @NonNull Class<T> type, @NonNull Consumer<T> readFunction, @NonNull Consumer<String> errorFunction) {
        super.read(relocate(location), type, readFunction, errorFunction);
    }

    @Override
    public <T> void write(@NonNull String location, T value, @NonNull Consumer<String> errorFunction) {
        super.write(relocate(location), value, errorFunction);
    }

    @Override
    public <T> void write(@NonNull String location, T value, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        super.write(relocate(location), value, successFunction, errorFunction);
    }
}
