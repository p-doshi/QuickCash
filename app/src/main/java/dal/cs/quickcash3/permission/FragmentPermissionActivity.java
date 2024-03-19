package dal.cs.quickcash3.permission;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class FragmentPermissionActivity extends FragmentActivity implements ActivityPermissionHandler {
    private final List<Consumer<PermissionResult>> permissionHandlers = new ArrayList<>();

    @Override
    public void registerPermissionHandler(@NonNull Consumer<PermissionResult> permissionHandler) {
        permissionHandlers.add(permissionHandler);
    }

    @Override
    public @NonNull Activity getActivity() {
        return this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Call all our permission handlers.
        PermissionResult result = new PermissionResult(requestCode, permissions, grantResults);
        for (Consumer<PermissionResult> permissionHandler : permissionHandlers) {
            permissionHandler.accept(result);
        }
    }
}
