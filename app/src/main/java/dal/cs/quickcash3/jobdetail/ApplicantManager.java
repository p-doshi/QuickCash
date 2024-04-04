package dal.cs.quickcash3.jobdetail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import dal.cs.quickcash3.R;
import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.Worker;
import dal.cs.quickcash3.database.Database;
import dal.cs.quickcash3.database.ObjectSearchAdapter;
import dal.cs.quickcash3.recycler.ApplicantViewHolder;
import dal.cs.quickcash3.recycler.RejectantViewHolder;
import dal.cs.quickcash3.recycler.WorkerRecyclerViewAdapter;
import dal.cs.quickcash3.search.ListSearchFilter;
import dal.cs.quickcash3.util.CustomObserver;

public class ApplicantManager {
    private static final String LOG_TAG = ApplicantManager.class.getSimpleName();
    private final Database database;
    private final AvailableJob job;
    private final Consumer<Worker> acceptFunction;
    private final WorkerRecyclerViewAdapter<ApplicantViewHolder> applicantsAdapter;
    private final WorkerRecyclerViewAdapter<RejectantViewHolder> rejectantsAdapter;
    private final AtomicInteger callbackId = new AtomicInteger();

    public ApplicantManager(@NonNull Database database, @NonNull AvailableJob job, @NonNull Consumer<Worker> acceptFunction) {
        this.database = database;
        this.job = job;
        this.acceptFunction = acceptFunction;
        this.applicantsAdapter = new WorkerRecyclerViewAdapter<>(
            R.layout.applicant,
            view -> new ApplicantViewHolder(view, this::acceptWorker, this::rejectWorker));
        this.rejectantsAdapter = new WorkerRecyclerViewAdapter<>(
            R.layout.rejectant,
            view -> new RejectantViewHolder(view, this::reconsiderWorker));
    }

    public void onCreate(@NonNull RecyclerView applicantsRecycler, @NonNull RecyclerView rejectedRecycler) {
        startUserSearch();
        applicantsRecycler.setAdapter(applicantsAdapter);
        rejectedRecycler.setAdapter(rejectantsAdapter);
    }

    private void startUserSearch() {
        Log.d(LOG_TAG, "Starting database search listener");
        database.removeListener(this.callbackId.get());
        applicantsAdapter.reset();
        rejectantsAdapter.reset();

        ListSearchFilter<Worker, String> searchFilter = new ListSearchFilter<>(Worker::key);
        searchFilter.setList(job.allApplicants());

        ObjectSearchAdapter<Worker> searchAdapter = new ObjectSearchAdapter<>(searchFilter);
        searchAdapter.addObserver(new CustomObserver<>(this::addWorker, this::removeWorker));

        int callbackId = database.addDirectoryListener(Worker.DIR, Worker.class, searchAdapter::receive,
            error -> Log.w(LOG_TAG, "Received database error: " + error));
        this.callbackId.set(callbackId);
    }

    private void addWorker(@NonNull Worker worker) {
        Log.v(LOG_TAG, "Added worker: " + worker);
        if (job.isApplicant(worker)) {
            applicantsAdapter.addWorker(worker);
        }
        else if (job.isRejectant(worker)) {
            rejectantsAdapter.addWorker(worker);
        }
        else {
            Log.w(LOG_TAG, "Received worker that is not on the job application");
        }
    }

    private void removeWorker(@NonNull Worker worker) {
        Log.v(LOG_TAG, "Removed worker: " + worker);
        if (job.isApplicant(worker)) {
            applicantsAdapter.removeWorker(worker);
        }
        else if (job.isRejectant(worker)) {
            rejectantsAdapter.removeWorker(worker);
        }
        else {
            Log.w(LOG_TAG, "Received worker that is not on the job application");
        }
    }

    private void acceptWorker(@NonNull Worker worker) {
        Log.v(LOG_TAG, "Accepted worker: " + worker);
        acceptFunction.accept(worker);
    }

    private void rejectWorker(@NonNull Worker worker) {
        Log.v(LOG_TAG, "Rejected worker: " + worker);
        job.rejectWorker(worker);
        applicantsAdapter.removeWorker(worker);
        rejectantsAdapter.addWorker(worker);
    }

    private void reconsiderWorker(@NonNull Worker worker) {
        Log.v(LOG_TAG, "Reconsidered worker: " + worker);
        job.reconsiderWorker(worker);
        rejectantsAdapter.removeWorker(worker);
        applicantsAdapter.addWorker(worker);
    }
}
