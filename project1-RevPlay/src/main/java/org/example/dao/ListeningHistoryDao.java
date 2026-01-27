package org.example.dao;

import org.example.config.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListeningHistoryDao {
    private static final Logger logger = LogManager.getLogger(ListeningHistoryDao.class);

    // --- LISTENING HISTORY ---
    public List<String> getListeningHistory(int userId) {
        List<String> history = new ArrayList<>();
        String view_history = "SELECT s.title, lh.listened_at " +
                "FROM listening_history lh " + "JOIN songs s ON lh.song_id = s.song_id " +
                "WHERE lh.user_id = ? " + "ORDER BY lh.listened_at DESC";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(view_history);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String record =
                        rs.getString("title") +
                                " | " +
                                rs.getTimestamp("listened_at");
                history.add(record);
            }
        } catch (SQLException se) {
            logger.error("Error fetching listening history", se);
        }
        return history;
    }

    // --- SAVE LISTENING HISTORY ---
    public void saveListeningHistory(int userId, int songId) {
        String sql = "INSERT INTO listening_history (user_id, song_id) VALUES (?, ?)";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        } catch (SQLException se) {
            logger.error("Error saving listening history", se);
        }
    }

    // --- RECENTLY PLAYED ----
    public List<String> getRecentlyPlayed(int userId, int limit) {
        List<String> recent = new ArrayList<>();
        String sql = "SELECT s.title, lh.listened_at " +
                        "FROM listening_history lh " + "JOIN songs s ON lh.song_id = s.song_id " +
                        "WHERE lh.user_id = ? " + "ORDER BY lh.listened_at DESC " + "LIMIT ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String row =
                        rs.getString("title") +
                                " | " +
                                rs.getTimestamp("listened_at");
                recent.add(row);
            }
        } catch (SQLException se) {
            logger.error("Error fetching recently played songs", se);
        }
        return recent;
    }


}
