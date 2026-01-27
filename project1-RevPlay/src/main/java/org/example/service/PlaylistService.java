package org.example.service;

import org.example.dao.PlaylistDao;
import org.example.dao.PlaylistSongDao;
import org.example.model.Playlist;

import java.util.List;

public class PlaylistService {
    private PlaylistDao playlistDao = new PlaylistDao();
    private PlaylistSongDao playlistSongDao = new PlaylistSongDao();

    public boolean createPlaylist(Playlist playlist) {
        if (playlist.getName() == null || playlist.getName().isBlank()) {
            return false;
        }
        if (!"PUBLIC".equalsIgnoreCase(playlist.getPrivacy()) &&
                !"PRIVATE".equalsIgnoreCase(playlist.getPrivacy())) {
            return false;
        }
        if (playlistDao.playlistExists(
                playlist.getUserId(),
                playlist.getName())) {
            return false;
        }
        return playlistDao.createPlaylist(playlist);
    }

    public boolean addSongToPlaylist(int userId, int playlistId, int songId) {

        // Ownership check (important)
        if (!playlistDao.isPlaylistOwnedByUser(playlistId, userId)) {
            return false;
        }
        return playlistSongDao.addSongToPlaylist(playlistId, songId);
    }

    public boolean removeSongFromPlaylist(int userId, int playlistId, int songId) {

        // ownership check
        if (!playlistDao.isPlaylistOwnedByUser(playlistId, userId)) {
            return false;
        }
        return playlistSongDao.removeSongFromPlaylist(playlistId, songId);
    }

    public List<String> getSongsInPlaylist(int playlistId) {
        return playlistDao.getSongsInPlaylist(playlistId);
    }

    public List<Playlist> getUserPlaylists(int userId) {
        return playlistDao.getPlaylistsByUser(userId);
    }

    public boolean updatePlaylist(Playlist playlist) {
        if (playlist.getName() == null || playlist.getName().isBlank()) {
            return false;
        }
        if (!"PUBLIC".equalsIgnoreCase(playlist.getPrivacy()) &&
                !"PRIVATE".equalsIgnoreCase(playlist.getPrivacy())) {
            return false;
        }
        return playlistDao.updatePlaylist(playlist);
    }

    public boolean deletePlaylist(int playlistId, int userId) {
        return playlistDao.deletePlaylist(playlistId, userId);
    }

    public List<String> viewPublicPlaylists(int userId) {
        return playlistDao.getPublicPlaylists(userId);
    }



}
