package com.genius.service;

import com.genius.data.DataStore;
import com.genius.model.Album;
import com.genius.model.Artist;
import com.genius.model.Song;

import java.time.LocalDate;
import java.util.List;

public class AlbumService {

    public static Album createAlbum(String title, LocalDate releaseDate, Artist artist) {
        Album album = new Album(title, releaseDate, artist.getUsername());
        DataStore.albums.put(album.getId(), album);
        artist.addAlbum(album.getId());
        return album;
    }

    public static void addSongsToAlbum(String albumId, List<String> songIds) {
        Album album = getAlbumById(albumId);
        if (album == null) return;
        for (String id : songIds) {
            album.addSong(id);
            Song song = SongService.getSongById(id);
            if (song != null) {
                song.setAlbumId(albumId);
                SongService.updateSong(song);
            }
        }
    }

    public static Album getAlbumById(String id) {
        return DataStore.albums.get(id);
    }

    public static String getArtist(String id) {
        return DataStore.albums.get(id).getArtistUsername();
    }
    public static List<Album> getAllAlbums() {
        return List.copyOf(DataStore.albums.values());
    }
    public static void createAlbum(String title, LocalDate releaseDate, String artistUsername, List<String> songIds) {
        Album album = new Album(title, releaseDate, artistUsername);
        songIds.forEach(album::addSong);  // Add songs in the provided order
        DataStore.albums.put(album.getId(), album); // Save the album to DataStore
    }

    public static List<Album> getAllAlbumsByArtist(String username) {
        return getAllAlbums().stream()
                .filter(e -> e.getArtistUsername() != null && e.getArtistUsername().equals(username))
                .toList();
    }
    public static void removeSongFromAlbum(String albumId, String songId) {
        Album album = getAlbumById(albumId);
        if (album != null) {
            Song song = SongService.getSongById(songId);
            if (song != null) {
                song.setAlbumId(null);
                album.removeSong(songId);
                SongService.updateSong(song);
            }
        }
    }


}
