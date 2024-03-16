package dal.cs.quickcash3.permission;

import androidx.annotation.NonNull;

/**
 * Store permission request codes here so that we can avoid conflicting codes.
 */
public enum PermissionRequestCode {
    LOCATION(87);

    private final int code;

    PermissionRequestCode(int code) {
        this.code = code;
    }

    /**
     * Get the permission request code with the matching code. Otherwise, throws an exception.
     *
     * @param code The code to search for.
     * @return The matching permission request code.
     */
    public static @NonNull PermissionRequestCode get(int code) {
        for (PermissionRequestCode requestCode : values()) {
            if (requestCode.code == code) {
                return requestCode;
            }
        }
        throw new IllegalArgumentException("Unrecognized Permission Request Code: " + code);
    }

    public int getCode() {
        return code;
    }
}
