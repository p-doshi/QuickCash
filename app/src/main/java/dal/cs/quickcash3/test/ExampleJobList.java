package dal.cs.quickcash3.test;

import static dal.cs.quickcash3.test.ExampleUserList.EMPLOYER1;
import static dal.cs.quickcash3.test.ExampleUserList.EMPLOYER2;
import static dal.cs.quickcash3.test.ExampleUserList.WORKERS;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import dal.cs.quickcash3.data.AvailableJob;
import dal.cs.quickcash3.data.JobUrgency;
import dal.cs.quickcash3.database.Database;

@SuppressLint("VisibleForTests") // This is a common test code class.
public final class ExampleJobList {
    public static final LatLng GOOGLEPLEX = new LatLng(37.42205852363422, -122.08523377972396);
    public static final LatLng DALHOUSIE = new LatLng(44.63562977946508, -63.59517486744167);
    public static final LatLng NEW_YORK = new LatLng(40.78255295453477, -73.96558364067354);
    public static final List<AvailableJob> AVAILABLE_JOBS;
    public static final String JOB_ID1 = "S0OGyNIfHY3wHtQDWkUf";

    static {
        AVAILABLE_JOBS = new ArrayList<>();

        AvailableJob job1 = AvailableJob.createForTest(JOB_ID1);
        job1.setTitle("Walk Dog");
        job1.setEmployer(EMPLOYER1);
        job1.setDescription("Please take my dog for a quick walk in Byxbee park.");
        job1.setLatitude(37.4402896497676);
        job1.setLongitude(-122.11344316636351);
        job1.setSalary(20);
        job1.setStartDate(new Date().toString());
        job1.setDuration(2);
        job1.setUrgency(JobUrgency.LOW.getValue());
        job1.setPostTime(new Date().toString());
        AVAILABLE_JOBS.add(job1);

        AvailableJob job2 = AvailableJob.createForTest("VWrRJpSrxGMHR4A4rzIK");
        job2.setTitle("Groceries");
        job2.setEmployer(EMPLOYER2);
        job2.setDescription("I need someone to get my groceries from the Walmart nearby.");
        job2.setLatitude(37.397619652667686);
        job2.setLongitude(-122.11391466754046);
        job2.setSalary(30);
        job2.setStartDate(new Date().toString());
        job2.setDuration(1);
        job2.setUrgency(JobUrgency.MEDIUM.getValue());
        job2.setPostTime(new Date().toString());
        AVAILABLE_JOBS.add(job2);

        AvailableJob job3 = AvailableJob.createForTest("WNBMabNMuFJ77zxl4zKC");
        job3.setTitle("Lawn Mowing");
        job3.setEmployer(EMPLOYER2);
        job3.setDescription("My lawn is getting a bit long, I need it mowed.");
        job3.setLatitude(37.4363059817969);
        job3.setLongitude(-122.25907955283132);
        job3.setSalary(50);
        job3.setStartDate(new Date().toString());
        job3.setDuration(2);
        job3.setUrgency(JobUrgency.LOW.getValue());
        job3.setPostTime(new Date().toString());
        AVAILABLE_JOBS.add(job3);

        AvailableJob job4 = AvailableJob.createForTest("Yh9umH7M3wHrQIDytHt2");
        job4.setTitle("Coding problem");
        job4.setEmployer(EMPLOYER1);
        job4.setDescription("I need help with a coding challenge.");
        job4.setLatitude(37.422305113453845);
        job4.setLongitude(-122.08556157502771);
        job4.setSalary(10);
        job4.setStartDate(new Date().toString());
        job4.setDuration(1);
        job4.setUrgency(JobUrgency.HIGH.getValue());
        job4.setPostTime(new Date().toString());
        List<String> applicants = new ArrayList<>();
        WORKERS.forEach(worker -> applicants.add(worker.key()));
        job4.setApplicants(applicants);
        AVAILABLE_JOBS.add(job4);

        AvailableJob job5 = AvailableJob.createForTest("kMyYthELmkdYDJcPqyx8");
        job5.setTitle("Move stuff");
        job5.setEmployer("RvdqabB039Gi5bi28t6Yc7w6GZi5LL");
        job5.setDescription("We are moving out of our house and need help moving the boxes to the house down the road.");
        job5.setLatitude(37.348898187024794);
        job5.setLongitude(-122.19771845591828);
        job5.setSalary(100);
        job5.setStartDate(new Date().toString());
        job5.setDuration(4);
        job5.setUrgency(JobUrgency.HIGH.getValue());
        job5.setPostTime(new Date().toString());
        AVAILABLE_JOBS.add(job5);

        AvailableJob job6 = AvailableJob.createForTest("p7isI5Y7mydwQAcDBPvY");
        job6.setTitle("Taxi");
        job6.setEmployer("hVGqoIf0OexpFyeaoYF93pyIFBuM0T");
        job6.setDescription("Can someone drive me to the Palo Alto Airport?");
        job6.setLatitude(37.38208882215943);
        job6.setLongitude(-122.14229872327587);
        job6.setSalary(50);
        job6.setStartDate(new Date().toString());
        job6.setDuration(1);
        job6.setUrgency(JobUrgency.HIGH.getValue());
        job6.setPostTime(new Date().toString());
        AVAILABLE_JOBS.add(job6);

        AvailableJob job7 = AvailableJob.createForTest("rnvH3iXFlB6KVBB8qPbZ");
        job7.setTitle("Tutoring");
        job7.setEmployer("9U1lYu2cOxintlymWnKGK9iAaGYXoA");
        job7.setDescription("I need help studying for my exam next week.");
        job7.setLatitude(37.4247885650797);
        job7.setLongitude(-122.1655333698144);
        job7.setSalary(180);
        job7.setStartDate(new Date().toString());
        job7.setDuration(6);
        job7.setUrgency(JobUrgency.MEDIUM.getValue());
        job7.setPostTime(new Date().toString());
        AVAILABLE_JOBS.add(job7);

        AvailableJob job8 = AvailableJob.createForTest("u1ZyFSonrqpBcJTHMt7t");
        job8.setTitle("Landscaping");
        job8.setEmployer("zhkHSMYnzCUXPLyCImfu8Rkw2ToTaA");
        job8.setDescription("My lawn is yellow and I need someone to make it green.");
        job8.setLatitude(37.39110006520576);
        job8.setLongitude(-122.11709836453915);
        job8.setSalary(1000);
        job8.setStartDate(new Date().toString());
        job8.setDuration(72);
        job8.setUrgency(JobUrgency.LOW.getValue());
        job8.setPostTime(new Date().toString());
        AVAILABLE_JOBS.add(job8);

        AvailableJob job9 = AvailableJob.createForTest("vg1iTndJtajiSwolDdf9");
        job9.setTitle("Plumbing");
        job9.setEmployer("k7UGobzWQQIM8EQaBVTs6FtkrVvQ5P");
        job9.setDescription("The toilet is clogged :)");
        job9.setLatitude(37.219474383643664);
        job9.setLongitude(-121.76440311519744);
        job9.setSalary(50);
        job9.setStartDate(new Date().toString());
        job9.setDuration(2);
        job9.setUrgency(JobUrgency.HIGH.getValue());
        job9.setPostTime(new Date().toString());
        AVAILABLE_JOBS.add(job9);

        AvailableJob job10 = AvailableJob.createForTest("wMEtg4fKZOG1NpGDmfbu");
        job10.setTitle("Snow Removal");
        job10.setEmployer("W65nwRuLsRiK2ZMISiQOyRJFkFo1GS");
        job10.setDescription("The apartment parking lot was never cleared, can someone clear it?");
        job10.setLatitude(44.64750397736993);
        job10.setLongitude(-63.623137524779466);
        job10.setSalary(200);
        job10.setStartDate(new Date().toString());
        job10.setDuration(6);
        job10.setUrgency(JobUrgency.HIGH.getValue());
        job10.setPostTime(new Date().toString());
        AVAILABLE_JOBS.add(job10);
    }

    // Utility class.
    private ExampleJobList() {}

    public static void generateJobPosts(@NonNull Database database, @NonNull Consumer<String> errorFunction) {
        AVAILABLE_JOBS.forEach(job -> job.writeToDatabase(database, errorFunction));
    }
}
