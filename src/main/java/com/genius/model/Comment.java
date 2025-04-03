package com.genius.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class Comment {
    private final Account account;
    private final String content;
    private final LocalDate date;
    private int likeCount;
    private Set<String> likedBy;  // To track users who liked this comment

    public Comment(Account account, String content, Song song) {
        this.account = account;
        this.content = content;
        this.date = LocalDate.now(); // current timestamp
        song.addComment(this);
        likeCount = 0;
        this.likedBy = new HashSet<>();
    }
    public void like(String username) {
        if (!likedBy.contains(username)) {
            likedBy.add(username);
            likeCount++;  // Increment like count if user hasn't liked already
        } else {
            likedBy.remove(username);  // Remove like if user unlikes
            likeCount--;  // Decrement like count
        }
    }
    public boolean isLiked(String username) {
        return likedBy.contains(username);
    }

    public int getLikeCount() {
        return likeCount;
    }

    public LocalDate getDate() {
        return date;
    }
    @Override
    public String toString() {
        return "username: "+account.getName()+", content: "+content+", date: "+date+ " (" + likeCount + " likes)";
    }


}
