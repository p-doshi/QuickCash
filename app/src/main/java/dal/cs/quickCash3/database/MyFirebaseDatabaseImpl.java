package dal.cs.quickCash3.database;

import android.content.Context;

import androidx.annotation.NonNull;

import dal.cs.quickCash3.R;
import dal.cs.quickCash3.database.Database;
import com.google.firebase.database.FirebaseDatabase;
import java.util.function.Consumer;

/**
 * DO NOT use this class to interact with the database!
 * Please use MyFirebaseDatabase instead.
 */
public class MyFirebaseDatabaseImpl implements Database {
    private final FirebaseDatabase database;

    public MyFirebaseDatabaseImpl(@NonNull Context context) {
        // Get a reference to the database.
        database = FirebaseDatabase.getInstance(context.getString(R.string.FIREBASE_DB_URL));
    }

    @Override
    public <T> void write(@NonNull String location, T value, @NonNull Consumer<String> errorFunction) {
        database.getReference(location).setValue(value)
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }

    @Override
    public <T> void write(@NonNull String location, T value, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        database.getReference(location).setValue(value)
            .addOnSuccessListener(unused -> successFunction.run())
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }


    @Override
    public <T> void read(@NonNull String location, @NonNull Class<T> type, @NonNull Consumer<T> readFunction, @NonNull Consumer<String> errorFunction) {
        database.getReference(location).get()
            .addOnSuccessListener(dataSnapshot -> {
                T value = dataSnapshot.getValue(type);
                readFunction.accept(value);
            })
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }
}
