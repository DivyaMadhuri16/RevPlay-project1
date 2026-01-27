package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.config.DBConnection;
import org.example.model.Song;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FavoriteDao {
    private static final Logger logger = LogManager.getLogger(FavoriteDao.class);

    // --- ADD FAVORITE ---
    public boolean addFavorite(int userId, int songId) {
        String add_favorite = "INSERT INTO favorites (user_id, song_id) VALUES (?, ?)";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(add_favorite);
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch (SQLException se) {
            logger.error("Error adding favorite", se);
            return false;
        }
    }

    // --- REMOVE FAVORITES ---
    public boolean removeFavorite(int userId, int songId) {
        String remove_favorite = "DELETE FROM favorites WHERE user_id = ? AND song_id = ?";

        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(remove_favorite);
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch (SQLException se) {
            logger.error("Error removing favorite", se);
            return false;
        }
    }

    // --- VIEW FAVORITE ---
    public List<Song> getFavorites(int userId) {
        String get_favorites = "SELECT s.song_id, s.title " + "FROM songs s JOIN favorites f ON s.song_id = f.song_id " + "WHERE f.user_id = ?";
        List<Song> songs = new ArrayList<>();
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(get_favorites);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                songs.add(song);
            }
        } catch (SQLException se) {
            logger.error("Error fetching favorites", se);
        }
        return songs;
    }


}