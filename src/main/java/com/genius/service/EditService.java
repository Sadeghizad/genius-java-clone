package com.genius.service;

import com.genius.data.DataStore;
import com.genius.model.*;

import java.util.List;

public class EditService {

    public static void suggestEdit(String songId, String newLyrics, String username) {
        if (!DataStore.songs.containsKey(songId)) throw new IllegalArgumentException("Song not found.");
        if (!DataStore.accounts.containsKey(username)) throw new IllegalArgumentException("User not found.");

        LyricEdit edit = new LyricEdit(songId, newLyrics, username);
        DataStore.lyricEdits.add(edit);
    }

    public static List<LyricEdit> getPendingEditsForSong(String songId) {
        return DataStore.lyricEdits.stream()
                .filter(e -> e.getSongId().equals(songId) && e.getStatus() == LyricEdit.Status.PENDING)
                .toList();
    }

    public static void approveEdit(LyricEdit edit) {
        Song song = DataStore.songs.get(edit.getSongId());
        if (song != null) {
            song.setLyrics(edit.getSuggestedLyrics());
            edit.approve();
        }
    }

    public static void rejectEdit(LyricEdit edit) {
        edit.reject();
    }

    public static List<LyricEdit> getAllPendingEdits() {
        return DataStore.lyricEdits.stream()
                .filter(e -> e.getStatus() == LyricEdit.Status.PENDING)
                .toList();
    }
}
