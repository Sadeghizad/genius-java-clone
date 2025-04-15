package com.genius.model;

import com.genius.service.AlbumService;

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
        if (AlbumService.getAllAlbums().stream().anyMatch(a -> a.getTitle().equalsIgnoreCase(title))) {
            throw new IllegalArgumentException("Album title must be unique.");
        }
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

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Album: " + title + ", By: " + artistUsername + ", Released: " + releaseDate;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    public void removeSong(String songId){
        songIds.remove(songId);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Album other = (Album) obj;
        return title.equalsIgnoreCase(other.title);
    }

    @Override
    public int hashCode() {
        return title.toLowerCase().hashCode();
    }


}
