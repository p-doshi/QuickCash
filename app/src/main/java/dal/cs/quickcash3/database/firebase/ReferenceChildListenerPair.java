package dal.cs.quickcash3.database.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;

class ReferenceChildListenerPair implements FirebaseDatabaseListener {
    private final DatabaseReference reference;
    private final ChildEventListener listener;

    public ReferenceChildListenerPair(@NonNull DatabaseReference reference, @NonNull ChildEventListener listener) {
        this.reference = reference;
        this.listener = listener;
    }

    @Override
    public void remove() {
        reference.removeEventListener(listener);
    }
}
