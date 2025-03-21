package com.genius.model;

import java.util.Date;

public class Comment {
    private final Account account;
    private final String content;
    private final Date date;

    public Comment(Account account, String content, Song song) {
        this.account = account;
        this.content = content;
        this.date = new Date(); // current timestamp
        song.addComment(this);
    }

    @Override
    public String toString() {
        return "username: "+account.getName()+", content: "+content+", date: "+date;
    }
}
