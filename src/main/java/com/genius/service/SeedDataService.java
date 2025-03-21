package com.genius.service;

import com.genius.data.DataStore;
import com.genius.model.*;

import java.time.LocalDate;
import java.util.List;

public class SeedDataService {

    public static void generate() {
        // Admin
        Admin admin = new Admin("AdminGuy", 35, "admin@g.com", "admin", "admin123");
        DataStore.accounts.put(admin.getUsername(), admin);

        // Artist
        Artist artist = new Artist("Ali Artist", 30, "ali@music.com", "ali_artist", "pass123");
        DataStore.accounts.put(artist.getUsername(), artist);

        // User
        User user = new User("Sara Listener", 22, "sara@user.com", "sara", "1234");
        DataStore.accounts.put(user.getUsername(), user);

        // Song
        Song song = new Song("Midnight Sky", "These are some lyrics", "Pop",
                List.of("pop", "midnight"), LocalDate.of(2023, 5, 20), null, List.of(artist.getUsername()));
        DataStore.songs.put(song.getId(), song);
        artist.addSong(song.getId());

        // Comment
        Comment comment = new Comment(user, "Amazing track!", song);

        // Album
        Album album = new Album("Dreamland", LocalDate.of(2023, 5, 25), artist.getUsername());
        album.addSong(song.getId());
        DataStore.albums.put(album.getId(), album);
        artist.addAlbum(album.getId());

        // Lyric Edit
        LyricEdit edit = new LyricEdit(song.getId(), "These are some NEW lyrics", user.getUsername());
        DataStore.lyricEdits.add(edit);
    }
}
