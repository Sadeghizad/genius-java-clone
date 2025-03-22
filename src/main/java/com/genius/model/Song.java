package com.genius.model;

import java.time.LocalDate;
import java.util.*;

public class Song {
    private final String id = UUID.randomUUID().toString();
    private String title;
    private String lyrics;
    private String genre;
    private List<String> tags = new ArrayList<>();
    private LocalDate releaseDate;
    private int viewCount = 0;

    private String albumId; // null if single
    private final List<String> artistUsernames = new ArrayList<>();
    private final List<Comment> comments = new ArrayList<>();

    public Song(String title, String lyrics, String genre, List<String> tags, LocalDate releaseDate, String albumId, List<String> artistUsernames) {
        this.title = title;
        this.lyrics = lyrics;
        this.genre = genre;
        this.tags = tags;
        this.releaseDate = releaseDate;
        this.albumId = albumId;
        this.artistUsernames.addAll(artistUsernames);
    }

    public void incrementViews() {
        viewCount++;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public String getId() {
        return id;
    }

    public int getViewCount() {
        return viewCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getTitle() {
        return title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public String getGenre() {
        return genre;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public List<String> getArtistUsernames() {
        return artistUsernames;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Genre: " + genre + ", Views: " + viewCount;
    }
}
