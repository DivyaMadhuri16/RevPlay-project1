package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.config.DBConnection;
import org.example.model.Song;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SongDao {
    private static final Logger logger = LogManager.getLogger(SongDao.class);

    //
    public int getAnySongId() {
        String sql = "SELECT song_id FROM songs LIMIT 1";
        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("song_id");
            }
        } catch (SQLException e) {
            logger.error("Error fetching song id", e);
        }
        return -1;
    }


    // --- UPLOAD SONGS ---
    public boolean uploadSong(Song song) {
        String insert_song = "INSERT INTO songs (artist_id, title, genre, duration, release_date) " + "VALUES (?, ?, ?, ?, ?)";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(insert_song);
            ps.setInt(1, song.getArtistId());
            ps.setString(2, song.getTitle());
            ps.setString(3, song.getGenre());
            ps.setInt(4, song.getDuration());
            ps.setDate(5, Date.valueOf(song.getReleaseDate()));

            return ps.executeUpdate() > 0;
        } catch (SQLException se) {
            logger.error("Error uploading song", se);
            return false;
        }
    }

    // --- ARTIST SONGS WITHOUT ALBUMS ---
    public List<Song> getSongsByArtist(int artistId) {
        List<Song> songs = new ArrayList<>();
        String get_songs_by_artist = "SELECT song_id, title FROM songs WHERE artist_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(get_songs_by_artist);
            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                songs.add(song);
            }
        } catch (SQLException se) {
            logger.error("Error fetching songs", se);
        }
        return songs;
    }

    // --- ADD SONG TO ALBUM ---
    public boolean addSongToAlbum(int songId, int albumId) {
        String add_song_to_album = "UPDATE songs SET album_id = ? WHERE song_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(add_song_to_album);
            ps.setInt(1, albumId);
            ps.setInt(2, songId);

            return ps.executeUpdate() > 0;
        } catch (SQLException se) {
            logger.error("Error adding song to album", se);
            return false;
        }
    }

    // --- UPDATE SONG ---
    public boolean updateSong(Song song) {
        String update_song = "UPDATE songs SET title = ?, genre = ?, duration = ?, release_date = ? " + "WHERE song_id = ? AND artist_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(update_song);
            ps.setString(1, song.getTitle());
            ps.setString(2, song.getGenre());
            ps.setInt(3, song.getDuration());
            ps.setDate(4, Date.valueOf(song.getReleaseDate()));
            ps.setInt(5, song.getSongId());
            ps.setInt(6, song.getArtistId());

            return ps.executeUpdate() > 0;
        } catch (SQLException se) {
            logger.error("Error updating song", se);
            return false;
        }
    }

    // --- DELETE SONGS ---
    public boolean deleteSong(int songId, int artistId) {
        String delete_songs = "DELETE FROM songs WHERE song_id = ? AND artist_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(delete_songs);
            ps.setInt(1, songId);
            ps.setInt(2, artistId);

            return ps.executeUpdate() > 0;
        } catch (SQLException se) {
            logger.error("Error deleting song", se);
            return false;
        }
    }

    // --- VIEW PLAY COUNT ---
    public List<Song> getSongsWithPlayCount(int artistId) {
        List<Song> songs = new ArrayList<>();
        String play_count = "SELECT song_id, title, play_count " + "FROM songs WHERE artist_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(play_count);
            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setPlayCount(rs.getInt("play_count"));
                songs.add(song);
            }
        } catch (SQLException se) {
            logger.error("Error fetching play count", se);
        }
        return songs;
    }

    // --- UPDATE PLAY COUNT ---
    public void incrementPlayCount(int songId) {
        String sql = "UPDATE songs SET play_count = play_count + 1 WHERE song_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, songId);
            ps.executeUpdate();
        } catch (SQLException se) {
            logger.error("Error updating play count", se);
        }
    }

    // --- GET PLAYCOUNT ---
    public List<String> getPlayCountByArtist(int artistId) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT title, play_count FROM songs WHERE artist_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(
                        rs.getString("title") +
                                " | Plays: " +
                                rs.getInt("play_count")
                );
            }
        } catch (SQLException se) {
            logger.error("Error fetching play count", se);
        }
        return result;
    }

    // --- SONG SEARCH ---
    public List<Song> searchSongs(String keyword) {
        List<Song> songs = new ArrayList<>();
        String searchKey = "%" + keyword + "%";
        String search_songs = "SELECT song_id, title, genre " + "FROM songs WHERE title LIKE ? OR genre LIKE ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(search_songs);
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setGenre(rs.getString("genre"));
                songs.add(song);
            }
        } catch (SQLException se) {
            logger.error("Error searching songs", se);
        }
        return songs;
    }

    // --- BROWSE SONG BY GENRE ---
    public List<Song> browseSongsByGenre(String genre) {
        List<Song> songs = new ArrayList<>();
        String songs_by_genre = "SELECT song_id, title, genre FROM songs WHERE genre = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(songs_by_genre);
            ps.setString(1, genre);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setGenre(rs.getString("genre"));
                songs.add(song);
            }
        } catch (SQLException se) {
            logger.error("Error browsing songs by genre", se);
        }
        return songs;
    }

    // --- BROWSE SONG BY ARTIST ---
    public List<Song> browseSongsByArtist(int artistId) {
        List<Song> songs = new ArrayList<>();
        String song_by_artist = "SELECT s.song_id, s.title, a.artist_name " + "FROM songs s JOIN artists a ON s.artist_id = a.artist_id " + "WHERE a.artist_id = ?";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(song_by_artist);
            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                songs.add(song);
            }
        } catch (SQLException se) {
            logger.error("Error browsing songs by artist", se);
        }
        return songs;
    }

    // --- BROWSE SONG BY ALBUM ---
    public List<Song> browseSongsByAlbum(int albumId) {
        List<Song> songs = new ArrayList<>();
        String songs_by_album = "SELECT s.song_id, s.title, al.album_title " + "FROM songs s JOIN albums al ON s.album_id = al.album_id " + "WHERE al.album_id = ?";

        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(songs_by_album);
            ps.setInt(1, albumId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                songs.add(song);
            }
        } catch (SQLException se) {
            logger.error("Error browsing songs by album", se);
        }
        return songs;
    }

    // --- ARTIST SONG FAVORITED ---
    public List<String> getUsersWhoFavoritedArtistSongs(int artistId) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT s.title AS song_title, u.email AS user_email " +
                "FROM songs s " + "JOIN favorites f ON s.song_id = f.song_id " +
                "JOIN users u ON f.user_id = u.user_id " + "WHERE s.artist_id = ? " + "ORDER BY s.title";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String row =
                        "Song: " + rs.getString("song_title") +
                                " | Favorited by: " + rs.getString("user_email");
                result.add(row);
            }
        } catch (SQLException se) {
            logger.error("Error fetching artist favorite analytics", se);
        }
        return result;
    }

    // --- GET SONGS ---
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT song_id, title FROM songs";
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Song s = new Song();
                s.setSongId(rs.getInt("song_id"));
                s.setTitle(rs.getString("title"));
                songs.add(s);
            }
        } catch (SQLException e) {
            logger.error("Error fetching songs", e);
        }
        return songs;
    }

    //
    public boolean deleteSongByTitle(String title, int artistId) {
        String sql = "DELETE FROM songs WHERE title = ? AND artist_id = ?";
        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setInt(2, artistId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting song", e);
        }
        return false;
    }


}
