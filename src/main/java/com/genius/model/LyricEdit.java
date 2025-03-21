package com.genius.model;

import java.util.Date;
import java.util.UUID;

public class LyricEdit {
    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    private final String id = UUID.randomUUID().toString();
    private final String songId;
    private final String suggestedLyrics;
    private final String suggestedByUsername;
    private final Date date;
    private Status status = Status.PENDING;

    public LyricEdit(String songId, String suggestedLyrics, String suggestedByUsername) {
        this.songId = songId;
        this.suggestedLyrics = suggestedLyrics;
        this.suggestedByUsername = suggestedByUsername;
        this.date = new Date();
    }

    public void approve() {
        status = Status.APPROVED;
    }

    public void reject() {
        status = Status.REJECTED;
    }

    public String getSongId() {
        return songId;
    }

    public String getSuggestedLyrics() {
        return suggestedLyrics;
    }

    public String getSuggestedByUsername() {
        return suggestedByUsername;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Edit by " + suggestedByUsername + " on " + date + " — status: " + status;
    }
}
