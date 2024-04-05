package dal.cs.quickcash3.test;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dal.cs.quickcash3.data.Employer;
import dal.cs.quickcash3.data.Worker;
import dal.cs.quickcash3.database.Database;

@SuppressLint("VisibleForTests") // This is a common test code class.
public final class ExampleUserList {
    public final static List<Employer> EMPLOYERS;
    public final static List<Worker> WORKERS;
    public final static String EMPLOYER1 = "5uD10neFj73BcfdgLAPG1SbViAXWtW";
    public final static String EMPLOYER2 = "11garPSt5m6yPr7KhGfgLIdFXbZjlx";
    public final static String WORKER1;
    public final static String WORKER2;

    static {
        EMPLOYERS = new ArrayList<>();
        Employer employer1 = Employer.createForTest(EMPLOYER1);
        employer1.setFirstName("Parth");
        employer1.setLastName("Doshi");
        EMPLOYERS.add(employer1);

        Employer employer2 = Employer.createForTest(EMPLOYER2);
        employer2.setFirstName("Mathew");
        employer2.setLastName("Smith");
        EMPLOYERS.add(employer2);

        WORKERS = new ArrayList<>();
        Worker worker1 = Worker.createForTest("LKAOIUoasd01kl1ASDoknd19kladio");
        worker1.setFirstName("Ethan");
        worker1.setLastName("Rozee");
        WORKER1 = worker1.fullName();
        WORKERS.add(worker1);

        Worker worker2 = Worker.createForTest("98u1d1oj0ud1oijadoijaod010asdl");
        worker2.setFirstName("Hayley");
        worker2.setLastName("Vezeau");
        WORKER2 = worker2.fullName();
        WORKERS.add(worker2);
    }

    // Utility class.
    private ExampleUserList() {}

    public static void generateUsers(@NonNull Database database, @NonNull Consumer<String> errorFunction) {
        generateEmployers(database, errorFunction);
        generateWorkers(database, errorFunction);
    }

    public static void generateEmployers(@NonNull Database database, @NonNull Consumer<String> errorFunction) {
        EMPLOYERS.forEach(employer -> employer.writeToDatabase(database, errorFunction));
    }

    public static void generateWorkers(@NonNull Database database, @NonNull Consumer<String> errorFunction) {
        WORKERS.forEach(worker -> worker.writeToDatabase(database, errorFunction));
    }
}
