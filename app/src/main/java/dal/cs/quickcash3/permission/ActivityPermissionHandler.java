package dal.cs.quickcash3.permission;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public interface ActivityPermissionHandler {
    /**
     * Registers a permission handler to be called when permission results are received.
     *
     * @param permissionHandler The permission handler to register.
     */
    void registerPermissionHandler(@NonNull Consumer<PermissionResult> permissionHandler);

    /**
     * Returns the activity that receives the permissions.
     *
     * @return The activity that receives the permissions.
     */
    @NonNull Activity getActivity();
}
