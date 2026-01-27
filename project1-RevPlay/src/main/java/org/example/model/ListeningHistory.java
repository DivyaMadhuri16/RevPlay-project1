package org.example.model;

import java.time.LocalDateTime;

public class ListeningHistory {
    private int historyId;
    private int userId;
    private int songId;
    private LocalDateTime listenedAt;

    public ListeningHistory() {}

    public ListeningHistory(int userId, int songId){
        this.userId = userId;
        this.songId = songId;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
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

    public LocalDateTime getListenedAt() {
        return listenedAt;
    }

    public void setListenedAt(LocalDateTime listenedAt) {
        this.listenedAt = listenedAt;
    }
}
