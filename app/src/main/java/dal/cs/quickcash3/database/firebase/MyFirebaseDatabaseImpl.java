package dal.cs.quickcash3.database.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;

/**
 * DO NOT use this class to interact with the database!
 * Please use MyFirebaseDatabase instead.
 */
class MyFirebaseDatabaseImpl implements Database {
    private final FirebaseDatabase database;
    private final Map<Integer, FirebaseDatabaseListener> listenerMap = new TreeMap<>();
    private int nextListenerId;

    public MyFirebaseDatabaseImpl() {
        database = FirebaseDatabase.getInstance("https://csci3130-group-3-default-rtdb.firebaseio.com/");
    }

    private int addListener(FirebaseDatabaseListener listener) {
        int listenerId = nextListenerId++;
        listenerMap.put(listenerId, listener);
        return listenerId;
    }

    @Override
    public <T> void write(
        @NonNull String path,
        @NonNull T value,
        @NonNull Consumer<String> errorFunction)
    {
        database.getReference(path).setValue(value)
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }

    @Override
    public <T> void write(
        @NonNull String path,
        @NonNull T value,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        database.getReference(path).setValue(value)
            .addOnSuccessListener(unused -> successFunction.run())
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }


    @Override
    public <T> void read(
        @NonNull String path,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        database.getReference(path).get()
            .addOnSuccessListener(dataSnapshot -> {
                T value = dataSnapshot.getValue(type);
                readFunction.accept(value);
            })
            .addOnFailureListener(e -> errorFunction.accept(e.getMessage()));
    }

    @Override
    public <T> int addListener(
        @NonNull String path,
        @NonNull Class<T> type,
        @NonNull Consumer<T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        DatabaseReference reference = database.getReference(path);
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

        ReferenceListenerPair pair = new ReferenceListenerPair(reference, listener);
        return addListener(pair);
    }

    @Override
    public <T> int addDirectoryListener(
        @NonNull String directory,
        @NonNull Class<T> type,
        @NonNull BiConsumer<String, T> readFunction,
        @NonNull Consumer<String> errorFunction)
    {
        DatabaseReference reference = database.getReference(directory);
        ChildEventListener listener = reference.addChildEventListener(
            new MyChildEventListener<>(type, readFunction, errorFunction));

        FirebaseDatabaseListener pair = new ReferenceChildListenerPair(reference, listener);
        return addListener(pair);
    }

    @Override
    public void removeListener(int listenerId) {
        FirebaseDatabaseListener listener = listenerMap.remove(listenerId);
        if (listener != null) {
            listener.remove();
        }
    }

    @Override
    public void delete(@NonNull String path, @NonNull Consumer<String> errorFunction) {
        database.getReference(path).removeValue()
            .addOnFailureListener(error -> errorFunction.accept(error.getMessage()));
    }

    @Override
    public void delete(
        @NonNull String path,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        database.getReference(path).removeValue()
            .addOnSuccessListener(unused -> successFunction.run())
            .addOnFailureListener(error -> errorFunction.accept(error.getMessage()));
    }
}
