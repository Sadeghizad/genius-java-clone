package com.genius.ui;

import com.genius.model.Album;
import com.genius.model.Artist;
import com.genius.model.Song;
import com.genius.model.User;
import com.genius.service.*;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class UserStage extends BaseStage {
    public static void show(Stage stage, User user) {
        Label welcome = new Label("Welcome, " + user.getName());

        Button viewTopSongs = new Button("Top Songs");
        Button viewFollowedArtists = new Button("Followed Artists");
        Button viewNewSongs = new Button("New Songs from Followed Artists");
        Button exploreArtistProfile = new Button("Explore Artist Profile");
        Button exploreSongs = new Button("Explore Songs");
        Button exploreAlbums = new Button("Explore Album");
        Button logout = new Button("Logout");

        viewTopSongs.setOnAction(e -> {
            List<Song> top = ChartService.getTopSongs(5);

            // Create a ListView to display top songs
            ListView<Song> songListView = new ListView<>();
            songListView.getItems().addAll(top);

            // Set cell factory for the ListView
            songListView.setCellFactory(param -> new ListCell<Song>() {
                @Override
                protected void updateItem(Song song, boolean empty) {
                    super.updateItem(song, empty);
                    if (empty || song == null) {
                        setText(null);
                    } else {
                        setText(song.getTitle() + " (" + song.getViewCount() + " views)");
                    }
                }
            });

            // Add mouse click event to show song details
            songListView.setOnMouseClicked(event -> {
                Song selectedSong = songListView.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    SongStage.show(stage, selectedSong, user);  // Show song details in a new stage
                }
            });

            // Create layout for the stage
            Button back = new Button("Back");
            VBox layout = new VBox(10, new Label("Top Songs"), songListView,back);
            layout.setStyle("-fx-padding: 20");

            // Create and show the stage for top songs
            Stage topSongsStage = new Stage();
            topSongsStage.setScene(new Scene(layout, 500, 600));
            topSongsStage.setTitle("Top Songs");
            topSongsStage.show();
            back.setOnAction(q -> {
                // Close the current subStage using the `stage` parameter
                topSongsStage.close();
            });
        });

        viewFollowedArtists.setOnAction(e -> {
            var artists = FollowService.getFollowedArtists(user);

            // Create a ListView to display followed artists
            ListView<Artist> artistListView = new ListView<>();
            artistListView.getItems().addAll(artists);

            // Set cell factory for the ListView
            artistListView.setCellFactory(param -> new ListCell<Artist>() {
                @Override
                protected void updateItem(Artist artist, boolean empty) {
                    super.updateItem(artist, empty);
                    if (empty || artist == null) {
                        setText(null);
                    } else {
                        setText(artist.getName() + " (" + artist.getUsername() + ")");
                    }
                }
            });

            // Add mouse click event to show artist profile
            artistListView.setOnMouseClicked(event -> {
                Artist selectedArtist = artistListView.getSelectionModel().getSelectedItem();
                if (selectedArtist != null) {
                    ArtistProfileStage.show(stage, selectedArtist, user);  // Show artist profile in a new stage
                }
            });
            Button back = new Button("Back");
            // Create layout for the stage
            VBox layout = new VBox(10, new Label("Followed Artists"), artistListView,back);
            layout.setStyle("-fx-padding: 20");

            // Create and show the stage for followed artists
            Stage followedArtistsStage = new Stage();
            followedArtistsStage.setScene(new Scene(layout, 500, 600));
            followedArtistsStage.setTitle("Followed Artists");
            followedArtistsStage.show();
            back.setOnAction(q -> {
                // Close the current subStage using the `stage` parameter
                followedArtistsStage.close();
            });
        });
        viewNewSongs.setOnAction(e -> {
            var songs = FollowService.getNewSongsFromFollowedArtists(user);
            var unique = new ArrayList<>(new HashSet<>(songs));  // Remove duplicates

            // Create a ListView to display new songs from followed artists
            ListView<Song> songListView = new ListView<>();
            songListView.getItems().addAll(unique);

            // Set cell factory for the ListView
            songListView.setCellFactory(param -> new ListCell<Song>() {
                @Override
                protected void updateItem(Song song, boolean empty) {
                    super.updateItem(song, empty);
                    if (empty || song == null) {
                        setText(null);
                    } else {
                        setText(song.getTitle() + " (" + song.getViewCount() + " views)");
                    }
                }
            });

            // Add mouse click event to show song details
            songListView.setOnMouseClicked(event -> {
                Song selectedSong = songListView.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    SongStage.show(stage, selectedSong, user);  // Show song details in a new stage
                }
            });

            Button back = new Button("Back");
            // Create layout for the stage
            VBox layout = new VBox(10, new Label("New Songs from Followed Artists"), songListView,back);
            layout.setStyle("-fx-padding: 20");
            // Create and show the stage for new songs
            Stage newSongsStage = new Stage();
            newSongsStage.setScene(new Scene(layout, 500, 600));
            newSongsStage.setTitle("New Songs from Followed Artists");
            newSongsStage.show();
            back.setOnAction(q -> {
                // Close the current subStage using the `stage` parameter
                newSongsStage.close();
            });
        });

        exploreArtistProfile.setOnAction(e -> {
            // Create a list of all artists
            List<Artist> artists = AccountService.getAllArtists();  // Assuming UserService has a method to get all artists

            // Calculate the total view count for each artist based on their songs
            artists.forEach(artist -> {
                int totalViews = SongService.getSongsByArtist(artist.getUsername()).stream()
                        .mapToInt(Song::getViewCount)
                        .sum();
                artist.setTotalViews(totalViews);  // Assuming setTotalViews() is available in the User class
            });

            // Sort artists by total views initially
            artists.sort((a, b) -> b.getTotalViews() - a.getTotalViews());  // Sort by total views descending

            // Create UI elements for searching, filtering, and sorting
            TextField searchField = new TextField();
            searchField.setPromptText("Search...");

            ComboBox<String> filterBox = new ComboBox<>();
            filterBox.getItems().addAll("All", "A-Z", "Most Popular");
            filterBox.setValue("Most Popular");

            ListView<Artist> artistListView = new ListView<>();
            artistListView.getItems().addAll(artists);

            // Artist List Cell Factory
            artistListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Artist item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getName() + " (" + item.getTotalViews() + " views)");
                }
            });

            // Runnable to update the list based on search or filter
            Runnable updateList = () -> {
                String keyword = searchField.getText().toLowerCase();
                String selectedSort = filterBox.getValue();

                var filteredArtists = artists.stream()
                        .filter(a -> a.getName().toLowerCase().contains(keyword));

                switch (selectedSort) {
                    case "A-Z" -> filteredArtists = filteredArtists.sorted(Comparator.comparing(Artist::getName));
                    case "Most Popular" -> filteredArtists = filteredArtists.sorted((a, b) -> b.getTotalViews() - a.getTotalViews());
                    default -> filteredArtists = filteredArtists.sorted(Comparator.comparing(Artist::getName));
                }

                artistListView.getItems().setAll(filteredArtists.toList());
            };

            // Listen for search text changes
            searchField.textProperty().addListener((obs, oldVal, newVal) -> updateList.run());

            // Listen for sorting changes
            filterBox.setOnAction(f -> updateList.run());

            // Artist list item click handler
            artistListView.setOnMouseClicked(ev -> {
                Artist selectedArtist = artistListView.getSelectionModel().getSelectedItem();
                if (selectedArtist != null) {
                    ArtistProfileStage.show(stage, selectedArtist,user);  // Assuming ArtistStage.show() displays artist's profile
                }
            });
            Button back = new Button("Back");

            // Layout for the artist exploration page
            VBox layout = new VBox(10, new Label("Artists"), searchField, filterBox, artistListView,back);
            layout.setStyle("-fx-padding: 20");

            // Stage for displaying the artist exploration page
            Stage artistStage = new Stage();
            artistStage.setScene(new Scene(layout, 500, 600));
            artistStage.setTitle("Explore Artists");
            artistStage.show();
            back.setOnAction(q -> {
                // Close the current subStage using the `stage` parameter
                artistStage.close();
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
            Button back = new Button("Back");

            VBox box = new VBox(10, new Label("Albums"),searchField, filterBox, list,back);
            box.setStyle("-fx-padding: 20");

            Stage albumStage = new Stage();
            albumStage.setScene(new Scene(box, 500, 600));
            albumStage.setTitle("Explore Albums");
            albumStage.show();
            back.setOnAction(q -> {
                // Close the current subStage using the `stage` parameter
                albumStage.close();
            });
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
            Button back = new Button("Back");
            VBox layout = new VBox(10, new Label("All Songs"), searchField, sortBox, list,back);
            layout.setStyle("-fx-padding: 20");

            Stage songStage = new Stage();
            songStage.setScene(new Scene(layout, 500, 600));
            songStage.setTitle("Explore Songs");
            songStage.show();
            back.setOnAction(q -> {
                // Close the current subStage using the `stage` parameter
                songStage.close();
            });
        });

        logout.setOnAction(e -> new Main().start(stage));

        VBox layout = new VBox(10, welcome, viewTopSongs, viewFollowedArtists, viewNewSongs,exploreArtistProfile,exploreSongs,exploreAlbums, logout);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center");

        stage.setScene(new Scene(layout, 450, 500));
    }



    private static void showList(String header, List<Song> songs) {
        StringBuilder sb = new StringBuilder();
        for (Song s : songs) {
            sb.append(s.getTitle()).append(" (")
                    .append(s.getViewCount()).append(" views)\n");
        }
        showText(header, sb.toString());
    }
}