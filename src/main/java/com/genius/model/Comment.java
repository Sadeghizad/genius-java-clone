package com.genius.model;

import java.time.LocalDate;


public class Comment {
    private final Account account;
    private final String content;
    private final LocalDate date;

    public Comment(Account account, String content, Song song) {
        this.account = account;
        this.content = content;
        this.date = LocalDate.now(); // current timestamp
        song.addComment(this);
    }

    @Override
    public String toString() {
        return "username: "+account.getName()+", content: "+content+", date: "+date;
    }

    public LocalDate getDate() {
        return date;
    }

}
