package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseDirectory;

public class CompletedJob extends JobPost {
    private String employeeId;
    private String completionDate;

    @Override
    public void writeToDatabase(@NonNull Database database, @NonNull Consumer<String> errorFunction) {
        database.write(DatabaseDirectory.COMPLETED_JOBS.getValue(), this, errorFunction);
    }

    public @Nullable String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(@NonNull String employee) {
        this.employeeId = employee;
    }

    public @Nullable String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(@NonNull String completionDate) {
        this.completionDate = completionDate;
    }
}
