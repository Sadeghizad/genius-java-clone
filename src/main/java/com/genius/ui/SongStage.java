package com.genius.ui;

import com.genius.model.Artist;
import com.genius.model.Comment;
import com.genius.model.Song;
import com.genius.model.User;
import com.genius.service.CommentService;
import com.genius.service.EditService;
import com.genius.service.SongService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Comparator;

public class SongStage extends BaseStage {

    public static void show(Stage stage, Song song, User user) {
        SongService.incrementView(song.getId());

        // Labels for Song details
        Label title = new Label("Title: " + song.getTitle());
        Label lyrics = new Label("Lyrics:\n" + song.getLyrics());
        Label views = new Label("Views: " + song.getViewCount());
        Label artists = new Label("Artists: " + song.getArtistUsernames());

        // TextArea for lyrics editing (for user to suggest changes)
        TextArea lyricsArea = new TextArea(song.getLyrics());
        lyricsArea.setEditable(true); // Allow editing the lyrics

        // Button to submit lyric changes for review
        Button submitForReviewBtn = new Button("Submit for Review");
        submitForReviewBtn.setOnAction(e -> {
            String updatedLyrics = lyricsArea.getText();
            // Call service to suggest the edit for review
            if(updatedLyrics.equals("")||updatedLyrics==null||updatedLyrics.equals(song.getLyrics())) {
                showText("Error", "Lyrics cannot be empty or same");
            }else{
            EditService.suggestEdit(song.getId(), updatedLyrics, user.getUsername());
            showText("Submitted", "Your lyrics suggestion has been submitted for review.");
        }});

        // Comment Section
        TextArea commentInputArea = new TextArea();
        commentInputArea.setPromptText("Enter your comment here...");
        commentInputArea.setWrapText(true);
        // ListView to display the comments
        ListView<Comment> commentListView = new ListView<>();
        // Assuming CommentService.getComments() fetches a list of comments for the song
        commentListView.getItems().addAll(CommentService.getComments(song));
        commentListView.setCellFactory(param -> new ListCell<Comment>() {
            private final Button likeButton = new Button("Like");

            @Override
            protected void updateItem(Comment comment, boolean empty) {
                super.updateItem(comment, empty);
                if (empty || comment == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(comment.toString());
                    likeButton.setOnAction(e -> {
                        String username = user.getUsername();  // Replace with the logged-in user's username
                        comment.like(username);  // Call the like/unlike method
                        likeButton.setText(comment.isLiked(username) ? "Unlike" : "Like");
                        updateItem(comment, false);  // Refresh the cell to show updated like count
                    });
                    setGraphic(likeButton);  // Add the like button to the cell
                }
            }
        });

        ComboBox<String> sortBox = new ComboBox<>();
        sortBox.getItems().addAll("Sort by Time", "Sort by Likes");
        sortBox.setValue("Sort by Time");  // Set default sorting to "Sort by Time"
        sortBox.setOnAction(e -> {
            String selectedSort = sortBox.getValue();

            switch (selectedSort) {
                case "Sort by Time":
                    // Sort comments by timestamp (oldest to newest)
                    commentListView.getItems().sort(Comparator.comparing(Comment::getDate));
                    break;
                case "Sort by Likes":
                    // Sort comments by like count (most to least likes)
                    commentListView.getItems().sort((c1, c2) -> Integer.compare(c2.getLikeCount(), c1.getLikeCount()));
                    break;
            }
        });

        // Button to expand more comments (simulate "show more")
        Button showMoreCommentsBtn = new Button("Show More Comments");
        showMoreCommentsBtn.setOnAction(e -> {
            // Implement logic to show more comments (this could be loading more comments from the service)
            commentListView.getItems().clear();
            commentListView.getItems().addAll(CommentService.getMoreComments(song));
        });
        Button sendCommentBtn = new Button("Send Comment");
        sendCommentBtn.setOnAction(e -> {
            String commentText = commentInputArea.getText();
            if (!commentText.isEmpty()) {
                Comment comment = CommentService.addComment(user, commentText, song);
                commentInputArea.clear(); // Clear the input field after sending
                commentListView.getItems().add(comment);
                showText("Comment Added", "Your comment has been added.");
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(commentListView); // Assuming commentListView is your ListView
        scrollPane.setFitToHeight(true); // Make it adjust to height
        scrollPane.setFitToWidth(true);

        Button back = new Button("Back");
        back.setOnAction(q -> {
            // Close the current subStage using the `stage` parameter
            UserStage.show(stage,user);
        });
        // Layout for the song page
        VBox layout = new VBox(10, title, artists, views, lyricsArea, submitForReviewBtn, commentInputArea, sendCommentBtn, sortBox, scrollPane, showMoreCommentsBtn, back);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center-left");

        // Create and show the song page stage
        stage.setScene(new Scene(layout, 500, 600));
        stage.setTitle("Song: " + song.getTitle());
        stage.show();
    }

    public static void show(Stage stage, Song song, Artist artist) {
        // Display song details
        Label songTitle = new Label("Song Title: " + song.getTitle());
        Label songLyricsLabel = new Label("Lyrics: ");
        TextArea songLyricsArea = new TextArea(song.getLyrics());
        songLyricsArea.setEditable(false); // Make it non-editable initially

        // Button to allow editing lyrics
        Button editButton = new Button("Edit Lyrics");
        editButton.setOnAction(e -> {
            songLyricsArea.setEditable(true); // Enable editing
            editButton.setDisable(true);
        });

        // Button to submit edited lyrics
        Button submitButton = new Button("Submit Changes");
        submitButton.setOnAction(e -> {
            // Update song's lyrics in memory
            String newLyrics = songLyricsArea.getText();
            song.setLyrics(newLyrics);  // Update lyrics in song object
            SongService.updateSong(song);  // Persist the updated song

            showText("Song Updated", "Lyrics updated successfully!");

            songLyricsArea.setEditable(false); // Disable further editing
            editButton.setDisable(false);
        });
        Button back = new Button("Back");
        back.setOnAction(q -> {
            // Close the current subStage using the `stage` parameter
            ArtistStage.show(stage,artist);
        });


        // Layout for the song page
        VBox layout = new VBox(10, songTitle, songLyricsLabel, songLyricsArea, editButton, submitButton,back);
        layout.setStyle("-fx-padding: 20");

        // Create and show the song page stage
        Stage songPageStage = new Stage();
        songPageStage.setScene(new Scene(layout, 500, 600));
        songPageStage.setTitle("Song: " + song.getTitle());
        songPageStage.show();
    }

}
