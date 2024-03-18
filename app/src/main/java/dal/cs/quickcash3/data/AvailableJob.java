package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseDirectory;
import dal.cs.quickcash3.util.Copyable;
import dal.cs.quickcash3.util.RandomStringGenerator;

public class AvailableJob extends JobPost implements Copyable<AvailableJob> {
    private String startDate;
    private double duration;
    private String urgency;
    private String postTime;
    private List<String> applicants;
    private List<String> blackList;

    // Empty constructor needed for Firebase
    public AvailableJob() { 
        super();
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

    @Override
    public @NonNull String toString() {
        return super.toString() +
            "\nstartDate='" + startDate + '\'' +
            "\nduration=" + duration +
            "\nurgency='" + urgency + '\'' +
            "\npostTime='" + postTime + '\'' +
            "\napplicants=" + applicants +
            "\nblackList=" + blackList;
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
            DatabaseDirectory.AVAILABLE_JOBS.getValue() + RandomStringGenerator.generate(HASH_SIZE),
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
            DatabaseDirectory.AVAILABLE_JOBS.getValue() + key,
            getClass(),
            job -> {
                this.copyFrom(job);
                successFunction.run();
            },
            errorFunction);
    }

    @Override
    public void copyFrom(@NonNull AvailableJob other) {
        super.copyFrom(other);
        startDate = other.startDate;
        duration = other.duration;
        urgency = other.urgency;
        postTime = other.postTime;
        applicants = other.applicants;
        blackList = other.blackList;
    }
}
