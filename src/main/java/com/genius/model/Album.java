package com.genius.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Album {
    private final String id = UUID.randomUUID().toString();
    private String title;
    private LocalDate releaseDate;
    private final String artistUsername;
    private final List<String> songIds = new ArrayList<>();

    public Album(String title, LocalDate releaseDate, String artistUsername) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.artistUsername = artistUsername;
    }

    public void addSong(String songId) {
        songIds.add(songId);
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public String getId() {
        return id;
    }

    public String getArtistUsername() {
        return artistUsername;
    }

    @Override
    public String toString() {
        return "Album: " + title + ", By: " + artistUsername + ", Released: " + releaseDate;
    }
}
