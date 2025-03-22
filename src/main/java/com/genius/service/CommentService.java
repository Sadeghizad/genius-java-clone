package com.genius.service;

import com.genius.model.Account;
import com.genius.model.Comment;
import com.genius.model.Song;

import java.util.List;

public class CommentService {

    public static void addComment(Account user, String content, Song song) {
        new Comment(user, content, song); // constructor auto-adds to song
    }

    public static List<Comment> getComments(Song song) {
        return song.getComments();
    }
}
