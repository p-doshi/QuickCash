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

    public boolean isMatchingCode(@NonNull PermissionRequestCode requestCode) {
         return this.requestCode.equals(requestCode);
    }

    public boolean containsPermission(@NonNull String permission) {
        return permissions.contains(permission);
    }

    public boolean isPermissionSuccessful(@NonNull String permission) {
        int index = permissions.indexOf(permission);
        return grantResults.get(index) == PackageManager.PERMISSION_GRANTED;
    }
}
