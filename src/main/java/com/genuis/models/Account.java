package com.genuis.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class Account {
    private String name;
    private int age;
    private String email;
    private String username;
    private String password;
    private boolean isLoggedIn =false;
    private static Set<String> usernames=new HashSet<>();
    public enum Permission {
        READ,
        EDIT,
        EDIT_APPROVAL,
        DELETE,
        UPDATE,
        FOLLOW,
        COMMENT
    }
    private Set<Permission> permissions = new HashSet<>();

    public Account(String name, int age, String email, String username, String password) {
        if (name == null || name.isEmpty() ||
            age < 0 || age > 100 ||
            email == null || email.isEmpty() ||
            username == null || username.isEmpty() ||
            password == null || password.isEmpty() ||
            isUsernameTaken(username)) {
            throw new IllegalArgumentException("Invalid account details or username already taken.");
        }
        this.name = name;
        this.age = age;
        this.email = email;
        this.username = username;
        this.password = password;
        usernames.add(username);
    }

    public boolean isUsernameTaken(String username) {
        return usernames.contains(username);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void givePermissions(ArrayList<Permission> permissions) {
        for (Permission permission : permissions) {
            this.permissions.add(permission);
        }
    }
    public void login(String username, String password) {
        if(username.equals(this.username) && password.equals(this.password)) {
            isLoggedIn = true;
        }else{
            throw new IllegalArgumentException("Invalid username or password.");
        }
    }
    public void logout() {
        isLoggedIn = false;
    }
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
