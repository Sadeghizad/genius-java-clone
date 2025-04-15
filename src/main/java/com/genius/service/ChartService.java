package com.genius.service;

import com.genius.data.DataStore;
import com.genius.model.Song;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ChartService {

    public static List<Song> getTopSongs(int limit) {
        return DataStore.songs.values().stream()
                .sorted(Comparator.comparingInt(Song::getViewCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
