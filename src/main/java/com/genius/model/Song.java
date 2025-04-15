package com.genius.model;

import com.genius.service.AlbumService;

import java.time.LocalDate;
import java.util.*;

public class Song {
    private final String id = UUID.randomUUID().toString();
    private String title;
    private String lyrics;
    private String genre;
    private List<String> tags = new ArrayList<>();
    private final LocalDate releaseDate;
    private int viewCount = 0;

    private String albumId; // null if single
    private final List<String> artistUsernames = new ArrayList<>();
    private final List<Comment> comments = new ArrayList<>();
    private Set<String> viewedBy = new HashSet<>();



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
    public Set<String> getViewedBy() {
        if (viewedBy == null) viewedBy = new HashSet<>();
        return viewedBy;
    }
    public boolean addView(String username) {
        if (getViewedBy().contains(username)) return false;
        viewedBy.add(username);
        incrementViews();
        return true;
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

    public Album getAlbum() {
        return AlbumService.getAlbumById(albumId);
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Genre: " + genre + ", Views: " + viewCount;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
