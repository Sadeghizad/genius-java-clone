package com.genius.model;

import java.util.HashSet;
import java.util.Set;

public class Artist extends Account {

    private final Set<String> songIds = new HashSet<>();
    private final Set<String> albumIds = new HashSet<>();

    public Artist(String name, int age, String email, String username, String password) {
        super(name, age, email, username, password);
        givePermissions(Set.of(Permission.READ, Permission.EDIT, Permission.EDIT_APPROVAL, Permission.COMMENT));
    }

    public void addSong(String songId) {
        songIds.add(songId);
    }

    public void addAlbum(String albumId) {
        albumIds.add(albumId);
    }

    public Set<String> getSongIds() {
        return songIds;
    }

    public Set<String> getAlbumIds() {
        return albumIds;
    }

    @Override
    public String toString() {
        return "[Artist] " + super.toString();
    }
}
