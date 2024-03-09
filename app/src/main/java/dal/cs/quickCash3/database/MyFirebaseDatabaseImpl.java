package dal.cs.quickCash3.database;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import dal.cs.quickCash3.R;

/**
 * DO NOT use this class to interact with the database!
 * Please use MyFirebaseDatabase instead.
 */
public class MyFirebaseDatabaseImpl implements Database {
    private final FirebaseDatabase db;
    private final Map<Integer, ReferenceListenerPair> listenerMap = new TreeMap<>();
    private int nextId = 0;

    public MyFirebaseDatabaseImpl(Context context) {
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

    @Override
    public <T> int addListener(String location, Class<T> type, Consumer<T> readFunction, Consumer<String> errorFunction) {
        DatabaseReference reference = db.getReference(location);
        ValueEventListener listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                T value = snapshot.getValue(type);
                readFunction.accept(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorFunction.accept(error.getMessage());
            }
        });

        int id = nextId++;
        ReferenceListenerPair pair = new ReferenceListenerPair(reference, listener);
        listenerMap.put(id, pair);
        return id;
    }

    @Override
    public void removeListener(int listenerId) {
        ReferenceListenerPair pair = listenerMap.get(listenerId);
        if (pair == null) {
            throw new IllegalArgumentException("Could not find listener callback with ID: " + listenerId);
        }

        pair.getReference().removeEventListener(pair.getListener());
    }

    @Override
    public void delete(String location, Consumer<String> errorFunction) {
        db.getReference(location).removeValue()
            .addOnFailureListener(error -> errorFunction.accept(error.getMessage()));
    }

    @Override
    public void delete(String location, Runnable successFunction, Consumer<String> errorFunction) {
        db.getReference(location).removeValue()
            .addOnSuccessListener(unused -> successFunction.run())
            .addOnFailureListener(error -> errorFunction.accept(error.getMessage()));
    }
}
