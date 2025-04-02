package com.genius.ui;

import com.genius.model.Album;
import com.genius.model.Song;
import com.genius.model.User;
import com.genius.service.*;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class UserStage {
    public static void show(Stage stage, User user) {
        Label welcome = new Label("Welcome, " + user.getName());

        Button viewTopSongs = new Button("Top Songs");
        Button viewFollowedArtists = new Button("Followed Artists");
        Button viewNewSongs = new Button("New Songs from Followed Artists");
        Button commentBtn = new Button("Comment on a Song");
        Button exploreSongs = new Button("Explore Songs");
        Button exploreAlbums = new Button("Explore Album");
        Button logout = new Button("Logout");

        viewTopSongs.setOnAction(e -> {
            List<Song> top = ChartService.getTopSongs(5);
            showList("Top Songs", top);
        });

        viewFollowedArtists.setOnAction(e -> {
            var artists = FollowService.getFollowedArtists(user);
            StringBuilder sb = new StringBuilder();
            artists.forEach(a -> sb.append(a.getName()).append(" (").append(a.getUsername()).append(")\n"));
            showText("Followed Artists", sb.toString());
        });

        viewNewSongs.setOnAction(e -> {
            var songs = FollowService.getNewSongsFromFollowedArtists(user);
            var unique = new ArrayList<>(new HashSet<>(songs));
            showList("New Songs from Followed Artists", unique);
        });


        commentBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Enter Song ID to comment on:");
            dialog.showAndWait().ifPresent(songId -> {
                Song s = SongService.getSongById(songId);
                if (s == null) {
                    showText("Error", "Song not found.");
                    return;
                }
                TextInputDialog commentDialog = new TextInputDialog();
                commentDialog.setHeaderText("Enter your comment:");
                commentDialog.showAndWait().ifPresent(c -> {
                    CommentService.addComment(user, c, s);
                    showText("Done", "Comment added.");
                });
            });
        });

        exploreAlbums.setOnAction(e -> {
            TextField searchField = new TextField();
            searchField.setPromptText("Search...");
            ComboBox<String> filterBox = new ComboBox<>();
            filterBox.getItems().addAll("All", "Title A-Z", "Newest", "Oldest");
            filterBox.setValue("All");

            ListView<Album> list = new ListView<>();
            list.getItems().addAll(AlbumService.getAllAlbums());

            list.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Album album, boolean empty) {
                    super.updateItem(album, empty);
                    if (empty || album == null) setText("");
                    else setText(album.getTitle() + " - " + album.getReleaseDate());
                }
            });
            Runnable updateList = () -> {
                String keyword = searchField.getText().toLowerCase();
                String sort = filterBox.getValue();

                var albums = AlbumService.getAllAlbums().stream()
                        .filter(s -> s.getTitle().toLowerCase().contains(keyword));

                switch (sort) {
                    case "Title A-Z" -> albums = albums.sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
                    case "Newest" -> albums = albums.sorted((a, b) -> b.getReleaseDate().compareTo(a.getReleaseDate()));
                    case "Oldest" -> albums = albums.sorted((a, b) -> a.getReleaseDate().compareTo(b.getReleaseDate()));
                    default -> albums = albums.sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
                }

                list.getItems().setAll(albums.toList());
            };
            searchField.textProperty().addListener((obs, oldVal, newVal) -> updateList.run());
            filterBox.setOnAction(f -> {
                String selected = filterBox.getValue();
                var stream = AlbumService.getAllAlbums().stream();
                switch (selected) {
                    case "Title A-Z" -> stream = stream.sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
                    case "Newest" -> stream = stream.sorted((a, b) -> b.getReleaseDate().compareTo(a.getReleaseDate()));
                    case "Oldest" -> stream = stream.sorted((a, b) -> a.getReleaseDate().compareTo(b.getReleaseDate()));
                }
                list.getItems().setAll(stream.toList());
            });

            list.setOnMouseClicked(ev -> {
                Album selected = list.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    AlbumStage.show(stage, selected, user);
                }
            });

            VBox box = new VBox(10, new Label("Albums"),searchField, filterBox, list);
            box.setStyle("-fx-padding: 20");

            Stage albumStage = new Stage();
            albumStage.setScene(new Scene(box, 500, 600));
            albumStage.setTitle("Explore Albums");
            albumStage.show();
        });

        exploreSongs.setOnAction(e -> {
            TextField searchField = new TextField();
            searchField.setPromptText("Search...");

            ComboBox<String> sortBox = new ComboBox<>();
            sortBox.getItems().addAll("Title A-Z", "Most Viewed", "Least Viewed");
            sortBox.setValue("Title A-Z");

            ListView<Song> list = new ListView<>();
            list.getItems().addAll(SongService.getAllSongs());

            list.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Song song, boolean empty) {
                    super.updateItem(song, empty);
                    if (empty || song == null) setText("");
                    else setText(song.getTitle() + " (" + song.getViewCount() + " views)");
                }
            });

            Runnable updateList = () -> {
                String keyword = searchField.getText().toLowerCase();
                String sort = sortBox.getValue();

                var songs = SongService.getAllSongs().stream()
                        .filter(s -> s.getTitle().toLowerCase().contains(keyword));

                switch (sort) {
                    case "Most Viewed" -> songs = songs.sorted((a, b) -> b.getViewCount() - a.getViewCount());
                    case "Least Viewed" -> songs = songs.sorted((a, b) -> a.getViewCount() - b.getViewCount());
                    default -> songs = songs.sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
                }

                list.getItems().setAll(songs.toList());
            };

            searchField.textProperty().addListener((obs, oldVal, newVal) -> updateList.run());
            sortBox.setOnAction(e2 -> updateList.run());

            list.setOnMouseClicked(ev -> {
                Song selected = list.getSelectionModel().getSelectedItem();
                if (selected != null) SongStage.show(stage, selected, user);
            });

            VBox layout = new VBox(10, new Label("All Songs"), searchField, sortBox, list);
            layout.setStyle("-fx-padding: 20");

            Stage songStage = new Stage();
            songStage.setScene(new Scene(layout, 500, 600));
            songStage.setTitle("Explore Songs");
            songStage.show();
        });

        logout.setOnAction(e -> new Main().start(stage));

        VBox layout = new VBox(10, welcome, viewTopSongs, viewFollowedArtists, viewNewSongs, commentBtn,exploreSongs,exploreAlbums, logout);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center");

        stage.setScene(new Scene(layout, 450, 500));
    }

    private static void showText(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static void showList(String header, List<Song> songs) {
        StringBuilder sb = new StringBuilder();
        for (Song s : songs) {
            sb.append("[").append(s.getId()).append("] ")
                    .append(s.getTitle()).append(" (")
                    .append(s.getViewCount()).append(" views)\n");
        }
        showText(header, sb.toString());
    }
}