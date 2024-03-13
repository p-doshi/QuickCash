package dal.cs.quickcash3.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.DatabaseDirectory;

public class AvailableJob extends JobPost {
    private String timeEstimate;
    private String postTime;
    private List<String> applicants;
    private List<String> blackList;

    @Override
    public void writeToDatabase(@NonNull Database database, @NonNull Consumer<String> errorFunction) {
        database.write(DatabaseDirectory.AVAILABLE_JOBS.getValue(), this, errorFunction);
    }

    public @Nullable String getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(@NonNull String timeEstimate) {
        this.timeEstimate = timeEstimate;
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
