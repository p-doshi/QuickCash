package dal.cs.quickcash3.jobdetail;

import android.util.Log;

import androidx.annotation.NonNull;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.database.Database;

public class ApplicantsManager {
    private static final String LOG_TAG = ApplicantsManager.class.getSimpleName();
    private final Database database;
    private final String jobId;

    public ApplicantsManager(@NonNull Database database, @NonNull String jobId) {
        this.database = database;
        this.jobId = jobId;
        AvailableJob.readFromDatabase(database, jobId,
            job -> {
                // TODO: Do something.
            },
            error -> Log.e(LOG_TAG, error));
    }
}
