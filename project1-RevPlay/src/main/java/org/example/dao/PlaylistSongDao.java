package org.example.dao;

import org.example.config.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlaylistSongDao {
    private static final Logger logger = LogManager.getLogger(PlaylistSongDao.class);

    public boolean addSongToPlaylist(int playlistId, int songId) {
        String add_song = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?, ?)";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(add_song);
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding song to playlist", e);
        }
        return false;
    }

    // --- REMOVE SONG FROM PLAYLIST ---
    public boolean removeSongFromPlaylist(int playlistId, int songId) {
        String sql = "DELETE FROM playlist_songs WHERE playlist_id = ? AND song_id = ?";

        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error removing song from playlist", e);
        }
        return false;
    }

}
