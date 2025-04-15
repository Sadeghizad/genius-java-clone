package com.genius.service;

import com.genius.data.DataStore;
import com.genius.model.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SongService {

    public static Song createSong(String title, String lyrics, String genre, List<String> tags, LocalDate releaseDate, String albumId, List<String> artistUsernames) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Song title is required.");
        }
        if (lyrics == null || lyrics.trim().isEmpty()) {
            throw new IllegalArgumentException("Lyrics are required.");
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre is required.");
        }
        if (artistUsernames == null || artistUsernames.isEmpty()) {
            throw new IllegalArgumentException("At least one artist is required.");
        }
        Song song = new Song(title, lyrics, genre, tags, releaseDate, albumId, artistUsernames);
        if (albumId != null) {
            Album album = AlbumService.getAlbumById(albumId);
            if (album != null) album.addSong(song.getId());
        }
        DataStore.songs.put(song.getId(), song);

        for (String username : artistUsernames) {
            var acc = DataStore.accounts.get(username);
            if (acc instanceof Artist artist) {
                artist.addSong(song.getId());
            }
        }

        return song;
    }
    

    public static Song getSongById(String id) {
        return DataStore.songs.get(id);
    }

    public static void incrementView(String songId, String username) {
        Song song = DataStore.songs.get(songId);
        if (song != null) {
            boolean added = song.addView(username);
            if (added) updateSong(song);
        }
    }


    public static List<Song> getAllSongs() {
        return List.copyOf(DataStore.songs.values());
    }

    public static List<Song> getSongsByArtist(String username) {
        return getAllSongs().stream()
                .filter(e -> e.getArtistUsernames() != null && e.getArtistUsernames().contains(username))
                .toList();
    }

    public static List<Song> getSongsByAlbum(Album album) {
        return getAllSongs().stream()
                .filter(e -> e.getAlbum() != null && e.getAlbum().equals(album))
                .toList();
    }
    public static List<String> getAllTags() {
        Set<String> tags = new HashSet<>();
        for (Song song : DataStore.songs.values()) {
            tags.addAll(song.getTags()); // Add all tags from the song
        }
        return List.copyOf(tags); // Return the unique tags as a list
    }
    public static void updateSong(Song song) {
            DataStore.songs.put(song.getId(), song); // Save the updated song to the DataStore
    }
}
