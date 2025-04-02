package com.genius.service;

import com.genius.model.Account;
import com.genius.model.Comment;
import com.genius.model.Song;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CommentService {

    public static void addComment(Account user, String content, Song song) {
        new Comment(user, content, song); // constructor auto-adds to song
    }

    public static List<String> getComments(Song song) {
        // Fetch and return the top 3 comments as strings, sorted by date
        List<Comment> allComments = song.getComments();
        allComments.sort(Comparator.comparing(Comment::getDate)); // Sort by date
        List<String> topComments = new ArrayList<>();
        for (int i = 0; i < Math.min(3, allComments.size()); i++) {
            topComments.add(allComments.get(i).toString()); // Assuming Comment has a getText() method
        }
        return topComments;
    }

    public static List<String> getMoreComments(Song song) {
        // Return all comments as strings
        List<String> allComments = new ArrayList<>();
        for (Comment comment : song.getComments()) {
            allComments.add(comment.toString()); // Assuming Comment has a getText() method
        }
        return allComments;
    }


}
