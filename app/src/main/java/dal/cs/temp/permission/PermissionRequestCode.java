package dal.cs.temp.permission;

/**
 * Store permission request codes here so that we can avoid conflicting codes.
 */
public enum PermissionRequestCode {
    LOCATION(87);

    private final int value;

    PermissionRequestCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
