package org.example.service;

import org.example.dao.SongDao;
import org.example.model.Song;

import java.util.List;

public class SongService {
    private SongDao songDao = new SongDao();
    public List<Song> getSongsByArtist(int artistId) {
        return songDao.getSongsByArtist(artistId);
    }

    public boolean addSongToAlbum(int songId, int albumId) {
        return songDao.addSongToAlbum(songId, albumId);
    }

    public boolean uploadSong(Song song) {
        if (song.getTitle() == null || song.getTitle().isBlank()) {
            return false;
        }
        return songDao.uploadSong(song);
    }

    public void incrementPlayCount(int songId) {
        songDao.incrementPlayCount(songId);
    }

    public List<String> getArtistFavoritesAnalytics(int artistId) {
        return songDao.getUsersWhoFavoritedArtistSongs(artistId);
    }

    public List<Song> getAllSongs() {
        return songDao.getAllSongs();
    }


    public boolean updateSong(Song song) {
        return songDao.updateSong(song);
    }

    public boolean deleteSong(int songId, int artistId) {
        return songDao.deleteSong(songId, artistId);
    }

    public List<Song> getSongsWithPlayCount(int artistId) {
        return songDao.getSongsWithPlayCount(artistId);
    }

    public List<Song> browseByGenre(String genre) {
        return songDao.browseSongsByGenre(genre);
    }

    public List<Song> browseByArtist(int artistId) {
        return songDao.browseSongsByArtist(artistId);
    }

    public List<Song> browseByAlbum(int albumId) {
        return songDao.browseSongsByAlbum(albumId);
    }


}
