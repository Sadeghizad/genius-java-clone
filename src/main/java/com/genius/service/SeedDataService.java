package com.genius.service;

import com.genius.data.DataStore;
import com.genius.model.*;
import java.time.LocalDate;
import java.util.List;

public class SeedDataService {

    public static void generate() {
        // Admin
        Admin admin = new Admin("Admin1", 35, "admin@g.com", "admin", "admin123");
        DataStore.accounts.put(admin.getUsername(), admin);

        // Artist
        Artist artist1 = new Artist("Shervin", 28, "shervin@genius.com", "shervin", "pass1");
        artist1.approve();
        Artist artist2 = new Artist("Yas", 32, "yas@genius.com", "yas", "pass2");
        Artist artist3 = new Artist("Moein", 84, "moein@genius.com", "moein", "pass3");
        DataStore.accounts.put(artist1.getUsername(), artist1);
        DataStore.accounts.put(artist2.getUsername(), artist2);
        DataStore.accounts.put(artist3.getUsername(), artist3);

        // User
        User user1 = new User("Mina", 22, "mina@user.com", "mina", "1234");
        User user2 = new User("Reza", 20, "reza@user.com", "reza", "4321");
        DataStore.accounts.put(user1.getUsername(), user1);
        DataStore.accounts.put(user2.getUsername(), user2);

        // Song
        Song song1 = SongService.createSong("Iran", "Land of my heart", "Patriotic",
                List.of("country", "pride"), LocalDate.of(2022, 3, 1), null, List.of("shervin"));

        Song song2 = SongService.createSong("Freedom", "We fight for it", "Rap",
                List.of("freedom", "power"), LocalDate.of(2022, 6, 10), null, List.of("yas"));

        Song song3 = SongService.createSong("Together", "Let’s rise up", "Rap",
                List.of("unity", "hope"), LocalDate.of(2023, 1, 5), null, List.of("yas", "shervin"));

        // Album
        Album album1 = AlbumService.createAlbum("Voices", LocalDate.of(2023, 2, 1), artist2);
        AlbumService.addSongsToAlbum(album1.getId(), List.of(song2.getId(), song3.getId()));

        Album album2 = AlbumService.createAlbum("Echoes", LocalDate.of(2023, 7, 1), artist1);
        AlbumService.addSongsToAlbum(album2.getId(), List.of(song1.getId(), song3.getId()));

        Album album3 = AlbumService.createAlbum("Legends", LocalDate.of(2024, 3, 10), artist2);
        AlbumService.addSongsToAlbum(album3.getId(), List.of(song2.getId()));

        // Comments
        CommentService.addComment(user1, "So powerful!", song1);
        CommentService.addComment(user2, "This is 🔥", song2);
        CommentService.addComment(user1, "Love the message", song3);
        CommentService.addComment(user2, "Deep and inspiring", song1);
        CommentService.addComment(user1, "Truly patriotic!", song1);
        CommentService.addComment(user2, "Hits hard every time", song2);
        CommentService.addComment(user1, "Classic Yas!", song2);
        CommentService.addComment(user2, "United we stand!", song3);
        CommentService.addComment(user1, "Uplifting as always", song3);
        CommentService.addComment(user2, "Never gets old", song1);
        CommentService.addComment(user1, "Masterpiece", song2);
        CommentService.addComment(user2, "I feel this one", song3);
        CommentService.addComment(user1, "Another banger", song1);
        CommentService.addComment(user2, "Lyrics are 🔥", song3);
        CommentService.addComment(user1, "Meaningful track", song2);
        CommentService.addComment(user2, "Emotional delivery", song1);

        // Lyric edits
        EditService.suggestEdit(song1.getId(), "Updated: Heart of my land", "mina");
        EditService.suggestEdit(song2.getId(), "Updated: Fighting for rights", "reza");

        // Follows
        FollowService.followArtist(user1, "shervin");
        FollowService.followArtist(user1, "yas");
        FollowService.followArtist(user2, "yas");
        FollowService.followArtist(user2, "shervin");
        FollowService.followArtist(user1, "moein");
        FollowService.followArtist(user2, "moein");


        // Views
        SongService.incrementView(song1.getId(),user1.getUsername());
        SongService.incrementView(song1.getId(),user2.getUsername());
        SongService.incrementView(song2.getId(),user1.getUsername());
        SongService.incrementView(song3.getId(), user1.getUsername());
        SongService.incrementView(song3.getId(), user2.getUsername());
        SongService.incrementView(song2.getId(), user2.getUsername());
        SongService.incrementView(song1.getId(), user1.getUsername());
    }
}
