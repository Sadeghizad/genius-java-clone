package com.genius.service;

import com.genius.data.DataStore;
import com.genius.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class SearchService {

    public static List<Song> searchSongs(String keyword) {
        String key = keyword.toLowerCase();
        return DataStore.songs.values().stream()
                .filter(song ->
                        song.getTitle().toLowerCase().contains(key) ||
                                song.getLyrics().toLowerCase().contains(key) ||
                                song.getGenre().toLowerCase().contains(key) ||
                                song.getTags().stream().anyMatch(t -> t.toLowerCase().contains(key)))
                .sorted(Comparator.comparingInt(Song::getViewCount).reversed())
                .collect(Collectors.toList());
    }

    public static List<Album> searchAlbums(String keyword) {
        String key = keyword.toLowerCase();
        return DataStore.albums.values().stream()
                .filter(album -> album.getTitle().toLowerCase().contains(key))
                .collect(Collectors.toList());
    }

    public static List<Artist> searchArtists(String keyword) {
        String key = keyword.toLowerCase();
        return DataStore.accounts.values().stream()
                .filter(acc -> acc instanceof Artist)
                .map(acc -> (Artist) acc)
                .filter(artist -> artist.getName().toLowerCase().contains(key))
                .collect(Collectors.toList());
    }
}
