package dal.cs.quickCash3.permission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AppCompatPermissionActivity extends AppCompatActivity {
    private final List<Consumer<PermissionResult>> permissionHandlers = new ArrayList<>();

    /**
     * Registers a permission handler to be called when permission results are received.
     *
     * @param permissionHandler The permission handler to register.
     */
    public void registerPermissionHandler(@NonNull Consumer<PermissionResult> permissionHandler) {
        permissionHandlers.add(permissionHandler);
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
