package dal.cs.quickcash3.database;

import androidx.annotation.NonNull;

public enum DatabaseDirectory {
    AVAILABLE_JOBS("public/available_jobs/"),
    COMPLETED_JOBS("public/completed_jobs/");

    private final String value;

    DatabaseDirectory(@NonNull String value) {
        this.value = value;
    }

    public @NonNull String getValue() {
        return value;
    }
}
