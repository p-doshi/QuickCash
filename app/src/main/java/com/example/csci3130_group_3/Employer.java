package com.example.csci3130_group_3;

public class Employer extends User {
    String employer_id;

    Employer(String name, String id) {
        super(name);
        employer_id = id;
    }

    public String getEmployerId() {
        return employer_id;
    }
}
