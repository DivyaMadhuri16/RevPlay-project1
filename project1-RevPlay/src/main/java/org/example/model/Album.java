package org.example.model;

import java.time.LocalDate;

public class Album {
    private int albumId;
    private int artistId;
    private String albumTitle;
    private LocalDate releaseDate;

    public Album() {}

    public Album(int artistId, String albumTitle, LocalDate releaseDate){
        this.artistId = artistId;
        this.albumTitle = albumTitle;
        this.releaseDate = releaseDate;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}
