package org.example.dao;

import org.example.config.DBConnection;
import org.example.model.Artist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistDao {
    private static final Logger logger = LogManager.getLogger(ArtistDao.class);

    // --- ARTIST PROFILE EXIST ---
    public boolean artistProfileExists(int userId) {
        String check_artist_exists = "SELECT artist_id FROM artists WHERE user_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(check_artist_exists);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException se) {
            logger.error("Error checking artist profile", se);
            return false;
        }
    }

    // ----- CREATE ARTIST PROFILE -----
    public boolean createArtistProfile(Artist artist) {
        String insert_artist = "INSERT INTO artists (user_id, artist_name, bio, genre, social_links) VALUES (?, ?, ?, ?, ?)";
        try{
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(insert_artist);
            ps.setInt(1, artist.getUserId());
            ps.setString(2, artist.getArtistName());
            ps.setString(3, artist.getBio());
            ps.setString(4, artist.getGenre());
            ps.setString(5, artist.getSocialLinks());

            return ps.executeUpdate() > 0;
        } catch (SQLException se) {
            logger.error("Error creating artist profile", se);
            return false;
        }
    }

    // ----- VIEW ARTIST PROFILE ----
    public Artist getArtistByUserId(int userId) {
        String get_artist_by_user = "SELECT artist_id, artist_name, bio, genre, social_links " + "FROM artists WHERE user_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(get_artist_by_user);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Artist artist = new Artist();
                artist.setArtistId(rs.getInt("artist_id"));
                artist.setUserId(userId);
                artist.setArtistName(rs.getString("artist_name"));
                artist.setBio(rs.getString("bio"));
                artist.setGenre(rs.getString("genre"));
                artist.setSocialLinks(rs.getString("social_links"));
                return artist;
            }
        } catch (SQLException se) {
            logger.error("Error fetching artist profile", se);
        }
        return null;
    }

    // --- UPDATE ARTIST PROFILE ---
    public boolean updateArtistProfile(Artist artist) {
        String update_artist = "UPDATE artists SET artist_name = ?, bio = ?, genre = ?, social_links = ? " + "WHERE user_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(update_artist);
            ps.setString(1, artist.getArtistName());
            ps.setString(2, artist.getBio());
            ps.setString(3, artist.getGenre());
            ps.setString(4, artist.getSocialLinks());
            ps.setInt(5, artist.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException se) {
            logger.error("Error updating artist profile", se);
            return false;
        }
    }

    // --- ARTIST SEARCH ---
    public List<Artist> searchArtists(String keyword) {
        List<Artist> artists = new ArrayList<>();
        String searchKey = "%" + keyword + "%";
        String search_artists = "SELECT artist_id, artist_name, genre " + "FROM artists WHERE artist_name LIKE ? OR genre LIKE ?";

        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(search_artists);
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Artist artist = new Artist();
                artist.setArtistId(rs.getInt("artist_id"));
                artist.setArtistName(rs.getString("artist_name"));
                artist.setGenre(rs.getString("genre"));
                artists.add(artist);
            }
        } catch (SQLException se) {
            logger.error("Error searching artists", se);
        }
        return artists;
    }

    // -------------------------------
    public Integer getArtistIdByUserId(int userId) {
        String sql = "SELECT artist_id FROM artists WHERE user_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("artist_id");
            }
        } catch (SQLException e) {
            logger.error("Error fetching artist id by user id", e);
        }
        return null; // artist profile not created
    }

    //
    public int getArtistIdByName(String artistName) {
        String sql = "SELECT artist_id FROM artists WHERE artist_name = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, artistName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("artist_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //
    public boolean deleteArtistById(int artistId) {
        String sql = "DELETE FROM artists WHERE artist_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, artistId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteArtistByUserId(int userId) {
        String sql = "DELETE FROM artists WHERE user_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}