package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseDirectory;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class CompletedJob extends JobPost {
    private String worker;
    private String completionDate;
    private String payId;

    @Override
    public void writeToDatabase(@NonNull Database database, @NonNull Consumer<String> errorFunction) {
        writeToDatabase(database, () -> {}, errorFunction);
    }

    @Override
    public void writeToDatabase(
        @NonNull Database database,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        database.write(
            DatabaseDirectory.COMPLETED_JOBS.getValue() + RandomStringGenerator.generate(HASH_SIZE),
            this,
            successFunction,
            errorFunction);
    }

    public @Nullable String getWorker() {
        return worker;
    }

    public void setWorker(@NonNull String worker) {
        this.worker = worker;
    }

    public @Nullable String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(@NonNull String completionDate) {
        this.completionDate = completionDate;
    }

    public @Nullable String getPayId() {
        return payId;
    }

    public void setPayId(@NonNull String payId) {
        this.payId = payId;
    }
}
