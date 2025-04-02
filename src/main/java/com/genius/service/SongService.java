package com.genius.service;

import com.genius.data.DataStore;
import com.genius.model.Album;
import com.genius.model.Artist;
import com.genius.model.Song;

import java.time.LocalDate;
import java.util.List;

public class SongService {

    public static Song createSong(String title, String lyrics, String genre, List<String> tags, LocalDate releaseDate, String albumId, List<String> artistUsernames) {
        Song song = new Song(title, lyrics, genre, tags, releaseDate, albumId, artistUsernames);
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

    public static void incrementView(String songId) {
        Song song = DataStore.songs.get(songId);
        if (song != null) song.incrementViews();
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
    public static void updateSong(Song song) {
        DataStore.songs.put(song.getId(), song); // Save the updated song to the DataStore
    }
}
