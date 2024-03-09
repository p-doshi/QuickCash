package dal.cs.quickCash3.employer;

import dal.cs.quickCash3.User;

public class Employer extends User {
    protected String employerId;

    /**
     * Required by Firebase.
     */
    Employer() {
        super();
        this.employerId = "";
    }

    Employer(String name, String employerId) {
        //super(name);
        this.name = name;
        this.employerId = employerId;
    }

    public String getEmployerId() {
        return employerId;
    }
}
