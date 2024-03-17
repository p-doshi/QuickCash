package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

public enum JobUrgency {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    private final String value;

    JobUrgency(@NonNull String value) {
        this.value = value;
    }

    public @NonNull String getValue() {
        return value;
    }
}
