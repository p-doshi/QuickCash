package dal.cs.quickcash3.database.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

class ReferenceListenerPair implements FirebaseDatabaseListener {
    private final DatabaseReference reference;
    private final ValueEventListener listener;

    public ReferenceListenerPair(@NonNull DatabaseReference reference, @NonNull ValueEventListener listener) {
        this.reference = reference;
        this.listener = listener;
    }

    @Override
    public void remove() {
        reference.removeEventListener(listener);
    }
}
