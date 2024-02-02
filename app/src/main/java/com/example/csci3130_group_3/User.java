package com.example.csci3130_group_3;

public abstract class User {
    String name;

    User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
