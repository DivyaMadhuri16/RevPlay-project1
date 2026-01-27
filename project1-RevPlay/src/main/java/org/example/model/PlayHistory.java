package org.example.model;

import java.time.LocalDateTime;

public class PlayHistory {

    private int userId;
    private int songId;
    private LocalDateTime playedAt;

    public PlayHistory(int userId, int songId, LocalDateTime playedAt) {
        this.userId = userId;
        this.songId = songId;
        this.playedAt = playedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }
}