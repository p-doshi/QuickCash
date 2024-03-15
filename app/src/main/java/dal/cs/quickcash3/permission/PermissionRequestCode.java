package dal.cs.quickcash3.permission;

import androidx.annotation.NonNull;

/**
 * Store permission request codes here so that we can avoid conflicting codes.
 */
public enum PermissionRequestCode {
    LOCATION(87);

    private final int value;

    PermissionRequestCode(int value) {
        this.value = value;
    }

    public static @NonNull PermissionRequestCode get(int value) {
        for (PermissionRequestCode code : PermissionRequestCode.values()) {
            if (code.value == value) {
                return code;
            }
        }
        throw new IllegalArgumentException("Unrecognized Permission Request code: " + value);
    }

    public int getValue() {
        return value;
    }
}
