package dal.cs.quickcash3.util;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;

public class BackButtonListener extends OnBackPressedCallback {
    private final Runnable callback;

    public BackButtonListener(@NonNull Runnable callback) {
        super(true);
        this.callback = callback;
    }

    @Override
    public void handleOnBackPressed() {
        callback.run();
    }
}
