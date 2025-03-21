package com.genius.model;

import java.util.Set;

public class Admin extends Account {

    public Admin(String name, int age, String email, String username, String password) {
        super(name, age, email, username, password);
        givePermissions(Set.of(Permission.READ, Permission.EDIT_APPROVAL));
    }

    @Override
    public String toString() {
        return "[Admin] " + super.toString();
    }
}
