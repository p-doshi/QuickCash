package dal.cs.quickcash3.permission;

import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionResult {
    private final PermissionRequestCode requestCode;
    private final List<String> permissions;
    private final List<Integer> grantResults;

    @SuppressWarnings("PMD.UseVarargs") // This is the format of the permission results.
    public PermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length != grantResults.length) {
            throw new IllegalArgumentException("Permission result arrays are not of the same size");
        }

        this.requestCode = PermissionRequestCode.get(requestCode);
        this.permissions = Arrays.asList(permissions);
        this.grantResults = new ArrayList<>();
        for (int grantResult : grantResults) {
            this.grantResults.add(grantResult);
        }
    }

    /**
     * Checks whether the permission result has the same request code as the one provided.
     *
     * @param requestCode The request code to check for.
     * @return True if the request codes match; otherwise, false.
     */
    public boolean isMatchingCode(@NonNull PermissionRequestCode requestCode) {
         return this.requestCode.equals(requestCode);
    }

    /**
     * Checks whether the permission result contains the given permission.
     *
     * @param permission The permission to check for.
     * @return True if the permission is contained within the list of permissions; otherwise, false.
     */
    public boolean containsPermission(@NonNull String permission) {
        return permissions.contains(permission);
    }

    /**
     * Checks whether the given permission has been granted.
     *
     * @param permission The permission to check for.
     * @return True if the permission has been granted; otherwise, false.
     */
    public boolean isPermissionGranted(@NonNull String permission) {
        if (containsPermission(permission)) {
            int index = permissions.indexOf(permission);
            return grantResults.get(index) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
}
