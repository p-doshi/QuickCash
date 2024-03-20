package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseDirectory;
import dal.cs.quickcash3.util.Copyable;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class CompletedJob extends JobPost implements Copyable<CompletedJob> {
    private String worker;
    private String completionDate;
    private String payId;
    private String status;

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

    public @Nullable String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

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

    @Override
    public void readFromDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Consumer<String> errorFunction)
    {
        readFromDatabase(database, key, () -> {}, errorFunction);
    }

    @Override
    public void readFromDatabase(
        @NonNull Database database,
        @NonNull String key,
        @NonNull Runnable successFunction,
        @NonNull Consumer<String> errorFunction)
    {
        database.read(
            DatabaseDirectory.COMPLETED_JOBS.getValue() + key,
            getClass(),
            job -> {
                this.copyFrom(job);
                successFunction.run();
            },
            errorFunction);
    }

    @Override
    public void copyFrom(@NonNull CompletedJob other) {
        super.copyFrom(other);
        worker = other.worker;
        completionDate = other.completionDate;
        payId = other.payId;
        status = other.status;
    }
}
