package com.genuis.models;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public abstract class Account {
    private String name;
    private int age;
    private String email;
    private final String username;
    private String password;
    private static final Set<String> usernames=new HashSet<>();
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
        this.password = hashPassword(password);
        usernames.add(username);
    }

    public static boolean isUsernameTaken(String username) {
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
    public void givePermissions(Set<Permission> permissions) {
        for (Permission permission : permissions) {
            this.permissions.add(permission);
        }
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported");
        }
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Age: " + age + ", Email: " + email + ", Username: " + username;
    }
}
