package com.genius.service;

import com.genius.data.DataStore;
import com.genius.model.Album;
import com.genius.model.Artist;

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
        Album album = DataStore.albums.get(albumId);
        if (album == null) throw new IllegalArgumentException("Album not found.");

        for (String songId : songIds) {
            if (!DataStore.songs.containsKey(songId)) continue;
            album.addSong(songId);
        }
    }

    public static Album getAlbumById(String id) {
        return DataStore.albums.get(id);
    }
}
