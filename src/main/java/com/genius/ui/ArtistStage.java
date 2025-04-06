package com.genius.ui;

import com.genius.data.DataStore;
import com.genius.model.Account;
import com.genius.model.Album;
import com.genius.model.Artist;
import com.genius.model.Song;
import com.genius.service.*;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistStage extends BaseStage {
    public static void show(Stage stage, Artist artist) {
        Label welcome = new Label("Welcome, " + artist.getName());

        Button viewProfile = new Button("View Profile");
        Button createSong = new Button("Create New Song");
        Button createAlbum = new Button("Create New Album");
        Button manageSongs = new Button("Manage Songs");
        Button manageAlbums = new Button("Manage Albums");
        Button manageProfile = new Button("Manage Profile");
        Button approveEdits = new Button("Review Lyric Edits");
        Button logout = new Button("Logout");

        viewProfile.setOnAction(e -> {
            ArtistProfileStage.show(stage, artist, null);  // Assuming ArtistStage.show() displays artist's profile
        });


        createSong.setOnAction(e -> {
            // Text fields for song title and lyrics
            TextField titleField = new TextField();
            titleField.setPromptText("Enter Song Title");

            TextArea lyricsArea = new TextArea();
            lyricsArea.setPromptText("Enter Song Lyrics");

            // ComboBox for genre selection with predefined genres
            ComboBox<String> genreComboBox = new ComboBox<>();
            genreComboBox.getItems().addAll("Rock", "Pop", "Rap", "Classical", "Jazz", "Other");
            genreComboBox.setPromptText("Choose or enter a genre");

            // TextField for tags input (you can later modify it to use multi-selection or predefined tags)
            TextField tagsField = new TextField();
            tagsField.setPromptText("Enter song tags (comma separated)");

            // Album and artist inputs (optional)
            ComboBox<String> albumComboBox = new ComboBox<>();

// Populate ComboBox with albums from the artist
            List<Album> artistAlbums = AlbumService.getAllAlbumsByArtist(artist.getUsername());
            albumComboBox.getItems().addAll(
                    artistAlbums.stream().map(Album::getTitle).toList()
            );
            albumComboBox.setPromptText("Select Album");

            ListView<String> artistListView = new ListView<>();
            List<String> allArtistUsernames = DataStore.accounts.values().stream()
                    .filter(account -> account instanceof Artist)
                    .map(Account::getUsername)
                    .toList();
            artistListView.getItems().addAll(allArtistUsernames);
            artistListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            // Submit Button
            Button submitButton = new Button("Create Song");
            submitButton.setOnAction(event -> {
                String title = titleField.getText();
                String lyrics = lyricsArea.getText();
                String genre = genreComboBox.getValue();
                List<String> tags = List.of(tagsField.getText().split(","));
                List<String> artistUsernames = new ArrayList<>(artistListView.getSelectionModel().getSelectedItems());
                artistUsernames.add(artist.getUsername());
                String selectedAlbumTitle = albumComboBox.getValue();


                Album selectedAlbum = null;
                if (selectedAlbumTitle != null) {
                    // If album is selected, get the album by title
                    selectedAlbum = artistAlbums.stream()
                            .filter(album -> album.getTitle().equals(selectedAlbumTitle))
                            .findFirst()
                            .orElse(null);
                }
            try {
                // Create the song
                Song song= SongService.createSong(title, lyrics, genre, tags, LocalDate.now(), selectedAlbum != null ? selectedAlbum.getId() : null, artistUsernames);
                if (selectedAlbum != null) {
                    AlbumService.addSongsToAlbum(selectedAlbum.getId(), List.of(song.getId()));
                }
                showText("Done", "Song created successfully.");
                Stage songStage=(Stage) submitButton.getScene().getWindow();
                songStage.close();
            } catch (IllegalArgumentException ex) {
                // Show validation error message
                showText("Error", ex.getMessage());
            }
            });

            // Layout for the song creation form
            VBox layout = new VBox(10, titleField, lyricsArea, artistListView,genreComboBox, tagsField, albumComboBox, submitButton);
            layout.setStyle("-fx-padding: 20; -fx-alignment: center");

            // Create and show the stage
            Stage songStage = new Stage();
            songStage.setScene(new Scene(layout, 500, 400));
            songStage.setTitle("Create New Song");
            songStage.show();
        });



        createAlbum.setOnAction(e -> {
            // Step 1: Input for album title
            TextInputDialog titleDialog = new TextInputDialog();
            titleDialog.setHeaderText("Enter album title:");
            titleDialog.showAndWait().ifPresent(title -> {

                // Ensure the album title is unique
                if (AlbumService.getAllAlbums().stream().anyMatch(album -> album.getTitle().equalsIgnoreCase(title))) {
                    showText("Error", "Album title must be unique.");
                    return;  // Exit if album title is not unique
                }

                // Step 2: Automatically set release date to current date
                LocalDate releaseDate = LocalDate.now();  // Set current date as release date

                // Step 3: Show artist's songs in a ListView
                ListView<Song> songListView = new ListView<>();
                List<Song> artistSongs = SongService.getSongsByArtist(artist.getUsername()); // Fetch songs by artist
                songListView.getItems().addAll(artistSongs);

                songListView.setCellFactory(param -> new ListCell<>() {
                    @Override
                    protected void updateItem(Song item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) setText("");
                        else setText(item.getTitle());  // Display song title
                    }
                });

                // Step 4: Multi-selection for songs
                songListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                // Step 5: Button to confirm album creation
                Button addButton = new Button("Add to Album");
                addButton.setOnAction(addAction -> {
                    List<Song> selectedSongs = songListView.getSelectionModel().getSelectedItems();
                    List<String> songIds = new ArrayList<>();
                    selectedSongs.forEach(song -> songIds.add(song.getId()));  // Collect song IDs

                    // Step 6: Create the album and add selected songs
                    try {
                        AlbumService.createAlbum(title, releaseDate, artist.getUsername(), songIds);
                        showText("Album Created", "Your album has been created successfully!");
                        Stage subStage = (Stage) addButton.getScene().getWindow();  // Get the current stage
                        subStage.close();
                    } catch (IllegalArgumentException ex) {
                        showText("Error", ex.getMessage());
                    }

                });

                // Step 7: Layout for the album creation UI
                VBox albumLayout = new VBox(10, new Label("Select Songs for Album:"), songListView, addButton);
                albumLayout.setStyle("-fx-padding: 20");

                Stage albumStage = new Stage();
                albumStage.setScene(new Scene(albumLayout, 500, 600));
                albumStage.setTitle("Create Album");
                albumStage.show();
            });
        });
        manageSongs.setOnAction(e -> {
            ListView<Song> songListView = new ListView<>();
            List<Song> artistSongs = SongService.getSongsByArtist(artist.getUsername());
            songListView.getItems().addAll(artistSongs);

            songListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Song item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getTitle());
                }
            });

            songListView.setOnMouseClicked(ev -> {
                Song selectedSong = songListView.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    SongStage.show(stage, selectedSong, artist);  // Navigate to the song page to view/edit
                }
            });
            Button back = new Button("Back");
            back.setOnAction(q -> {
                Stage subStage = (Stage) back.getScene().getWindow();
                subStage.close();
            });
            VBox layout = new VBox(10, new Label("Your Songs"), songListView,back);
            layout.setStyle("-fx-padding: 20");

            Stage manageSongsStage = new Stage();
            manageSongsStage.setScene(new Scene(layout, 500, 600));
            manageSongsStage.setTitle("Manage Songs");
            manageSongsStage.show();
        });
        manageAlbums.setOnAction(e -> {
            ListView<Album> albumListView = new ListView<>();
            List<Album> artistAlbums = AlbumService.getAllAlbumsByArtist(artist.getUsername());
            albumListView.getItems().addAll(artistAlbums);

            albumListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Album item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getTitle());
                }
            });

            albumListView.setOnMouseClicked(ev -> {
                Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
                if (selectedAlbum != null) {
                    showAlbumEditWindow(selectedAlbum,artist);
                }
            });

            VBox layout = new VBox(10, new Label("Your Albums"), albumListView);
            layout.setStyle("-fx-padding: 20");

            Stage manageAlbumsStage = new Stage();
            manageAlbumsStage.setScene(new Scene(layout, 500, 600));
            manageAlbumsStage.setTitle("Manage Albums");
            manageAlbumsStage.show();
        });
        manageProfile.setOnAction(e->{});
        approveEdits.setOnAction(e -> {
            var edits = EditService.getAllPendingEdits().stream()
                    .filter(m -> artist.getSongIds().contains(m.getSongId()))
                    .toList();

            for (var edit : edits) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Edit for Song ID: " + edit.getSongId());
                alert.setContentText("Suggested by: " + edit.getSuggestedByUsername() + "\n\n" + edit.getSuggestedLyrics());
                ButtonType approve = new ButtonType("Approve");
                ButtonType reject = new ButtonType("Reject");
                alert.getButtonTypes().setAll(approve, reject, ButtonType.CANCEL);

                var result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == approve) EditService.approveEdit(edit);
                    else if (result.get() == reject) EditService.rejectEdit(edit);
                }
            }
        });

        logout.setOnAction(e -> new Main().start(stage));

        VBox layout = new VBox(10, welcome, viewProfile, createSong, createAlbum, approveEdits, manageProfile,manageSongs,manageAlbums, logout);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center");
        stage.setScene(new Scene(layout, 500, 500));
    }

    private static void showAlbumEditWindow(Album album, Artist artist) {
        List<Song> albumSongs = new ArrayList<>(SongService.getSongsByAlbum(album));
        ListView<Song> tracklistView = new ListView<>();
        tracklistView.getItems().addAll(albumSongs);
        ListView<Song> availableSongsView = new ListView<>();
        List<Song> availableSongs = new ArrayList<>(SongService.getSongsByArtist(artist.getUsername())
                .stream().filter(song -> !album.getSongIds().contains(song.getId()))
                .toList());
        System.out.println("Available songs: " + availableSongs.size());
        availableSongs.forEach(s -> System.out.println(s.getTitle()));
        System.out.println("not Available songs: " + albumSongs.size());
        albumSongs.forEach(s -> System.out.println(s.getTitle()));
        System.out.println("all songs: " + SongService.getSongsByArtist(artist.getUsername()).size());
        SongService.getSongsByArtist(artist.getUsername()).forEach(s -> System.out.println(s.getTitle()));



        tracklistView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });

        Button removeSongButton = new Button("Remove Song");
        removeSongButton.setOnAction(e -> {
            Song selectedSong = tracklistView.getSelectionModel().getSelectedItem();
            if (selectedSong != null) {
                AlbumService.removeSongFromAlbum(album.getId(), selectedSong.getId());
                tracklistView.getItems().remove(selectedSong);
                availableSongsView.getItems().add(selectedSong);
            }
        });

        // Add song selection functionality to add new songs to the album
        availableSongsView.getItems().addAll(availableSongs);

        availableSongsView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });

        Button addSongButton = new Button("Add Song");
        addSongButton.setOnAction(e -> {
            Song selectedSong = availableSongsView.getSelectionModel().getSelectedItem();
            if (selectedSong != null && !album.getSongIds().contains(selectedSong.getId())) {
                album.addSong(selectedSong.getId());
                tracklistView.getItems().add(selectedSong);
                availableSongsView.getItems().remove(selectedSong);
            }
        });

        VBox layout = new VBox(10, new Label("Tracklist for: " + album.getTitle()),
                tracklistView, removeSongButton,
                new Label("Available Songs to Add"), availableSongsView, addSongButton);
        layout.setStyle("-fx-padding: 20");

        Stage albumEditStage = new Stage();
        albumEditStage.setScene(new Scene(layout, 500, 600));
        albumEditStage.setTitle("Edit Album");
        albumEditStage.show();
    }
}
