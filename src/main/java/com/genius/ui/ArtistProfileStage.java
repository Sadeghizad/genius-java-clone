package com.genius.ui;

import com.genius.model.Album;
import com.genius.model.Artist;
import com.genius.model.Song;
import com.genius.model.User;
import com.genius.service.AlbumService;
import com.genius.service.SongService;
import com.genius.service.FollowService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArtistProfileStage extends BaseStage {

    public static void show(Stage stage, Artist artist, User loggedInUser) {
        // Get artist's albums and songs
        List<Album> albums = new ArrayList<>(AlbumService.getAllAlbumsByArtist(artist.getUsername()));
        List<Song> songs = new ArrayList<>(SongService.getSongsByArtist(artist.getUsername()));

        // Sort albums by release date and songs by view count
        albums.sort(Comparator.comparing(Album::getReleaseDate));
        songs.sort((a, b) -> b.getViewCount() - a.getViewCount());

        // Labels and list views for profile page
        Label nameLabel = new Label("Name: " + artist.getName());

        // Create the follow button only if the logged-in user is not the artist
        Button followButton;
        if (loggedInUser != null && !loggedInUser.getUsername().equals(artist.getUsername())) {
            followButton = new Button("Follow");

            // Check if the user is already following the artist
            if (FollowService.isFollowing(loggedInUser, artist.getUsername())) {
                followButton.setText("Unfollow");
            }

            followButton.setOnAction(e -> {
                if (FollowService.isFollowing(loggedInUser, artist.getUsername())) {
                    FollowService.unfollowArtist(loggedInUser, artist.getUsername());
                    followButton.setText("Follow");
                    showText("Unfollowed", "You are no longer following " + artist.getName());
                } else {
                    FollowService.followArtist(loggedInUser, artist.getUsername());
                    followButton.setText("Unfollow");
                    showText("Followed", "You are now following " + artist.getName());
                }
            });
        } else {
            followButton = null;
        }


        // Albums list view
        ListView<Album> albumListView = new ListView<>();
        albumListView.getItems().addAll(albums);
        albumListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Album item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });

        albumListView.setOnMouseClicked(ev -> {
            Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
            if (selectedAlbum != null && loggedInUser != null) {
                AlbumStage.show(stage, selectedAlbum, loggedInUser);
            }
        });

        // Songs list view
        ListView<Song> songListView = new ListView<>();
        songListView.getItems().addAll(songs);
        songListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText("");
                else {
                    // Highlight most viewed songs
                    setText(item.getTitle() + " (" + item.getViewCount() + " views)");
                    if (item.getViewCount() > 1000) {
                        setStyle("-fx-font-weight: bold; -fx-text-fill: #e7ce55; -fx-background-color: #e7ce55;");
                    }
                }
            }
        });

        songListView.setOnMouseClicked(ev -> {
            Song selectedSong = songListView.getSelectionModel().getSelectedItem();
            if (selectedSong != null && loggedInUser != null) {
                SongStage.show(stage, selectedSong, loggedInUser);  // Navigate to song page
            }
        });

        // Layout for the profile page
        VBox profileLayout = new VBox(10, nameLabel, new Label("Albums:"), albumListView, new Label("Songs:"), songListView);

        if (followButton != null) {
            profileLayout.getChildren().add(followButton);
        }

        profileLayout.setStyle("-fx-padding: 20; -fx-background-color: #e7ce55;");

        // Create and show the profile stage
        Stage profileStage = new Stage();
        profileStage.setScene(new Scene(profileLayout, 500, 600));
        profileStage.setTitle(artist.getName() + " - Profile");
        profileStage.show();
    }

}
