package com.genius.service;

import com.genius.data.DataStore;
import com.genius.model.*;

public class AccountService {

    public static Account signup(String name, int age, String email, String username, String password, String role) {
        if (Account.isUsernameTaken(username)) {
            throw new IllegalArgumentException("Username already taken.");
        }

        Account account;
        switch (role.toLowerCase()) {
            case "user" -> account = new User(name, age, email, username, password);
            case "artist" -> account = new Artist(name, age, email, username, password);
            case "admin" -> account = new Admin(name, age, email, username, password); // if allowed manually
            default -> throw new IllegalArgumentException("Invalid role.");
        }

        DataStore.accounts.put(username, account);
        return account;
    }

    public static Account login(String username, String password) {
        Account acc = DataStore.accounts.get(username);
        if (acc == null) throw new IllegalArgumentException("Account not found.");

        // hash input to compare with stored hashed password
        String hashedInput = hash(password);
        if (!acc.getPassword().equals(hashedInput)) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        return acc;
    }

    private static String hash(String password) {
        try {
            var digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing error.");
        }
    }
}
