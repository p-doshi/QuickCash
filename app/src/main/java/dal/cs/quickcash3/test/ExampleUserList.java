package dal.cs.quickcash3.test;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import dal.cs.quickcash3.data.Employer;
import dal.cs.quickcash3.data.User;
import dal.cs.quickcash3.data.UserRole;
import dal.cs.quickcash3.data.Worker;
import dal.cs.quickcash3.database.Database;

public final class ExampleUserList {
    public final static Map<String, User> USERS;
    public final static Map<String, Employer> EMPLOYERS;
    public final static Map<String, Worker> WORKERS;
    public final static String EMPLOYER1 = "5uD10neFj73BcfdgLAPG1SbViAXWtW";
    public final static String EMPLOYER2 = "11garPSt5m6yPr7KhGfgLIdFXbZjlx";
    public final static String WORKER1 = "LKAOIUoasd01kl1ASDoknd19kladio";
    public final static String WORKER2 = "98u1d1oj0ud1oijadoijaod010asdl";

    static {
        USERS = new TreeMap<>();
        EMPLOYERS = new TreeMap<>();
        WORKERS = new TreeMap<>();

        Employer user1 = new Employer();
        user1.setFirstName("Parth");
        user1.setLastName("Doshi");
        user1.setRole(UserRole.EMPLOYER.getValue());
        USERS.put(EMPLOYER1, user1);
        EMPLOYERS.put(EMPLOYER1, user1);

        Worker user2 = new Worker();
        user2.setFirstName("Ethan");
        user2.setLastName("Rozee");
        user2.setRole(UserRole.WORKER.getValue());
        USERS.put(WORKER1, user2);
        WORKERS.put(WORKER1, user2);

        Worker user3 = new Worker();
        user3.setFirstName("Hayley");
        user3.setLastName("Vezeau");
        user3.setRole(UserRole.WORKER.getValue());
        USERS.put(WORKER2, user3);
        WORKERS.put(WORKER2, user3);

        Employer user4 = new Employer();
        user4.setFirstName("Mathew");
        user4.setLastName("Smith");
        user4.setRole(UserRole.EMPLOYER.getValue());
        USERS.put(EMPLOYER2, user4);
        EMPLOYERS.put(EMPLOYER2, user4);
    }

    // Utility class.
    private ExampleUserList() {}

    public static void generateUsers(@NonNull Database database, @NonNull Consumer<String> errorFunction) {
        for (Map.Entry<String, User> entry : USERS.entrySet()) {
            entry.getValue().writeToDatabase(database, entry.getKey(), errorFunction);
        }
    }
}
