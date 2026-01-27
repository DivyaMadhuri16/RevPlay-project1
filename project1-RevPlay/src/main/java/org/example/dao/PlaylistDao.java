package org.example.dao;

import org.example.config.DBConnection;
import org.example.model.Playlist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDao {
    private static final Logger logger = LogManager.getLogger(PlaylistDao.class);

    // --- CREATE PLAYLIST ---
    public boolean createPlaylist(Playlist playlist) {
        String create_playlist = "INSERT INTO playlists (user_id, name, description, privacy) " + "VALUES (?, ?, ?, ?)";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(create_playlist);
            ps.setInt(1, playlist.getUserId());
            ps.setString(2, playlist.getName());
            ps.setString(3, playlist.getDescription());
            ps.setString(4, playlist.getPrivacy());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error creating playlist", e);
        }
        return false;
    }

    // --- PLAYLIST CHECK ---
    public boolean playlistExists(int userId, String name) {
        String sql = "SELECT 1 FROM playlists WHERE user_id = ? AND name = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, name);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            logger.error("Error checking playlist existence", e);
        }
        return false;
    }


    // --- PLAYLIST OWNED ---
    public boolean isPlaylistOwnedByUser(int playlistId, int userId) {
        String sql = "SELECT 1 FROM playlists WHERE playlist_id = ? AND user_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ps.setInt(2, userId);

            return ps.executeQuery().next();
        } catch (SQLException e) {
            logger.error("Error checking playlist ownership", e);
        }
        return false;
    }

    // --- GET SONG IN PLAYLIST
    public List<String> getSongsInPlaylist(int playlistId) {
        List<String> songs = new ArrayList<>();
        String sql = "SELECT s.song_id, s.title " + "FROM playlist_songs ps " +
                        "JOIN songs s ON ps.song_id = s.song_id " + "WHERE ps.playlist_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                songs.add(rs.getInt("song_id") + " : " + rs.getString("title"));
            }
        } catch (SQLException e) {
            logger.error("Error fetching playlist songs", e);
        }
        return songs;
    }

    // --- VIEW PLAYLIST ---
    public List<Playlist> getPlaylistsByUser(int userId) {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT playlist_id, name, description, privacy, created_at " + "FROM playlists WHERE user_id = ?";

        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Playlist p = new Playlist();
                p.setPlaylistId(rs.getInt("playlist_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrivacy(rs.getString("privacy"));
                p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                playlists.add(p);
            }
        } catch (SQLException e) {
            logger.error("Error fetching user playlists", e);
        }
        return playlists;
    }

    // --- UPDATE PLAYLIST ---
    public boolean updatePlaylist(Playlist playlist) {
        String sql = "UPDATE playlists SET name = ?, description = ?, privacy = ? " + "WHERE playlist_id = ? AND user_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, playlist.getName());
            ps.setString(2, playlist.getDescription());
            ps.setString(3, playlist.getPrivacy());
            ps.setInt(4, playlist.getPlaylistId());
            ps.setInt(5, playlist.getUserId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating playlist", e);
        }
        return false;
    }

    // --- DELETE PLATLIST ---
    public boolean deletePlaylist(int playlistId, int userId) {
        String sql = "DELETE FROM playlists WHERE playlist_id = ? AND user_id = ?";

        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting playlist", e);
        }
        return false;
    }

    // --- PUBLIC PLAYLIST ---
    public List<String> getPublicPlaylists(int currentUserId) {
        List<String> playlists = new ArrayList<>();

        String sql = "SELECT p.playlist_id, p.name, p.description, u.email " + "FROM playlists p " +
                     "JOIN users u ON p.user_id = u.user_id " + "WHERE p.privacy = 'PUBLIC' AND p.user_id <> ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                playlists.add(rs.getInt("playlist_id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getString("description") + " | Created by: " +
                                rs.getString("email")
                );
            }
        } catch (SQLException e) {
            logger.error("Error fetching public playlists", e);
        }
        return playlists;
    }

    //
    public boolean deletePlaylistByName(String name, int userId) {
        String sql = "DELETE FROM playlists WHERE name = ? AND user_id = ?";
        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            logger.error("Error deleting playlist", e);
        }
        return false;
    }


}
