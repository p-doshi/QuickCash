package dal.cs.quickcash3.database;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReferenceListenerPair {
    private final DatabaseReference reference;
    private final ValueEventListener listener;

    public ReferenceListenerPair(@NonNull DatabaseReference reference, @NonNull ValueEventListener listener) {
        this.reference = reference;
        this.listener = listener;
    }

    public @NonNull DatabaseReference getReference() {
        return reference;
    }

    public @NonNull ValueEventListener getListener() {
        return listener;
    }
}
