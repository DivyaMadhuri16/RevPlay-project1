package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.config.DBConnection;
import org.example.model.Album;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlbumDao {
    private static final Logger logger = LogManager.getLogger(AlbumDao.class);
    String insert_album = "INSERT INTO albums (artist_id, album_title, release_date) " + "VALUES (?, ?, ?)";
    public boolean createAlbum(Album album) {
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(insert_album);
            ps.setInt(1, album.getArtistId());
            ps.setString(2, album.getAlbumTitle());
            ps.setDate(3, Date.valueOf(album.getReleaseDate()));

            return ps.executeUpdate() > 0;
        } catch (SQLException se) {
            logger.error("Error creating album", se);
            return false;
        }
    }

    // --- GET ARTIST ALBUM ---
    public List<Album> getAlbumsByArtist(int artistId) {
        List<Album> albums = new ArrayList<>();
        String get_albums_by_artist = "SELECT album_id, album_title FROM albums WHERE artist_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(get_albums_by_artist);
            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Album album = new Album();
                album.setAlbumId(rs.getInt("album_id"));
                album.setAlbumTitle(rs.getString("album_title"));
                albums.add(album);
            }
        } catch (SQLException se) {
            logger.error("Error fetching albums", se);
        }
        return albums;
    }

    // --- UPDATE ALBUM ---
    public boolean updateAlbum(Album album) {
        String update_album = "UPDATE albums SET album_title = ?, release_date = ? " + "WHERE album_id = ? AND artist_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(update_album);
            ps.setString(1, album.getAlbumTitle());
            ps.setDate(2, Date.valueOf(album.getReleaseDate()));
            ps.setInt(3, album.getAlbumId());
            ps.setInt(4, album.getArtistId());

            return ps.executeUpdate() > 0;
        } catch (SQLException se) {
            logger.error("Error updating album", se);
            return false;
        }
    }

    // --- ALBUM SEARCH ---
    public List<Album> searchAlbums(String keyword) {
        List<Album> albums = new ArrayList<>();
        String searchKey = "%" + keyword + "%";
        String search_albums = "SELECT album_id, album_title " + "FROM albums WHERE album_title LIKE ?";

        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(search_albums);
            ps.setString(1, searchKey);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Album album = new Album();
                album.setAlbumId(rs.getInt("album_id"));
                album.setAlbumTitle(rs.getString("album_title"));
                albums.add(album);
            }
        } catch (SQLException se) {
            logger.error("Error searching albums", se);
        }
        return albums;
    }


}
