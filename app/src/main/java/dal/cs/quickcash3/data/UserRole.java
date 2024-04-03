package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

public enum UserRole {
    EMPLOYER("employer"),
    WORKER("worker");

    private final String value;

    UserRole(@NonNull String value) {
        this.value = value;
    }

    public @NonNull String getValue() {
        return value;
    }
}
