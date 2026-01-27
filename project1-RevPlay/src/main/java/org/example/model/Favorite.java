package org.example.model;

public class Favorite {
    private int userId;
    private int songId;

    public Favorite() {}

    public Favorite(int userId, int songId){
        this.userId = userId;
        this.songId = songId;
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
}
