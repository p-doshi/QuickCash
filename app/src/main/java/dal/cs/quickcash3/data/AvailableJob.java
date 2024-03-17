package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.google.firebase.database.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseDirectory;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class AvailableJob extends JobPost {
    private String startDate;
    private double duration;
    private String urgency;
    private String postTime;
    private List<String> applicants;
    private List<String> blackList;

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
            DatabaseDirectory.AVAILABLE_JOBS.getValue() + RandomStringGenerator.generate(HASH_SIZE),
            this,
            successFunction,
            errorFunction);
    }

    public @Nullable String getStartDate() {
        return startDate;
    }

    public void setStartDate(@NonNull String startDate) {
        this.startDate = startDate;
    }

    public @Nullable String getUrgency() {
        return urgency;
    }

    public void setUrgency(@NonNull String urgency) {
        this.urgency = urgency;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public @Nullable String getPostTime() {
        return postTime;
    }

    public void setPostTime(@NonNull String postTime) {
        this.postTime = postTime;
    }

    public @Nullable List<String> getApplicants() {
        return applicants;
    }

    public void setApplicants(@NonNull List<String> applicants) {
        this.applicants = applicants;
    }

    public @Nullable List<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(@NonNull List<String> blackList) {
        this.blackList = blackList;
    }
}
