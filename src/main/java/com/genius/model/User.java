package com.genius.model;

import java.util.HashSet;
import java.util.Set;

public class User extends Account {

    private final Set<String> followedArtists = new HashSet<>();

    public User(String name, int age, String email, String username, String password) {
        super(name, age, email, username, password);
        givePermissions(Set.of(Permission.READ, Permission.FOLLOW, Permission.COMMENT, Permission.EDIT));
    }

    public void followArtist(String artistUsername) {
        followedArtists.add(artistUsername);
    }

    public void unfollowArtist(String artistUsername) {
        followedArtists.remove(artistUsername);
    }

    public Set<String> getFollowedArtists() {
        return followedArtists;
    }

    @Override
    public String toString() {
        return "[User] " + super.toString();
    }
}
