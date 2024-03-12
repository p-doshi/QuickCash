package dal.cs.quickcash3.database;

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

import dal.cs.quickcash3.R;

/**
 * DO NOT use this class to interact with the database!
 * Please use MyFirebaseDatabase instead.
 */
public class MyFirebaseDatabaseImpl implements Database {
    private final FirebaseDatabase database;
    private final Map<Integer, ReferenceListenerPair> listenerMap = new TreeMap<>();
    private int nextListenerId;

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

    @Override
    public <T> int addListener(@NonNull String location, @NonNull Class<T> type, @NonNull Consumer<T> readFunction, @NonNull Consumer<String> errorFunction) {
        DatabaseReference reference = database.getReference(location);
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

        int listenerId = nextListenerId++;
        ReferenceListenerPair pair = new ReferenceListenerPair(reference, listener);
        listenerMap.put(listenerId, pair);
        return listenerId;
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
    public void delete(@NonNull String location, @NonNull Consumer<String> errorFunction) {
        database.getReference(location).removeValue()
            .addOnFailureListener(error -> errorFunction.accept(error.getMessage()));
    }

    @Override
    public void delete(@NonNull String location, @NonNull Runnable successFunction, @NonNull Consumer<String> errorFunction) {
        database.getReference(location).removeValue()
            .addOnSuccessListener(unused -> successFunction.run())
            .addOnFailureListener(error -> errorFunction.accept(error.getMessage()));
    }
}
