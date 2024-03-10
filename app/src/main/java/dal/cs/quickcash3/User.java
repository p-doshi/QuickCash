package dal.cs.quickcash3;

/**
 * This is not abstract so that we can instantiate a generic user when reading from a database.
 */
public class User {
    protected String name;

    /**
     * Required by Firebase.
     */
    public User() {
        name = "";
    }

    User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
