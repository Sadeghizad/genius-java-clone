package com.genius.service;

import com.genius.model.Account;
import com.genius.model.Comment;
import com.genius.model.Song;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CommentService {

    public static Comment addComment(Account user, String content, Song song) {
        if (user.getPermissions().contains(Account.Permission.COMMENT)) {
            Comment comment = new Comment(user, content, song); // constructor auto-adds to song
            return comment;
        } else {
            throw new IllegalArgumentException("You do not have permission to comment on this song.");
        }
    }

    public static List<Comment> getComments(Song song) {
        // Fetch and return the top 3 comments as strings, sorted by date
        List<Comment> allComments = song.getComments();
        allComments.sort(Comparator.comparing(Comment::getDate)); // Sort by date
        return allComments.size() > 3 ? allComments.subList(0, 3) : allComments;
    }

    public static List<Comment> getMoreComments(Song song) {
        // Return all comments as strings
        List<Comment> allComments = song.getComments();
        allComments.sort(Comparator.comparing(Comment::getDate)); // Sort by date
        return allComments;
    }


}
