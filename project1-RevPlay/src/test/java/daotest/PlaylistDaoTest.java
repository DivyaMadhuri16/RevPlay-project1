package daotest;

import org.example.dao.PlaylistDao;
import org.example.dao.UserDao;
import org.example.model.Playlist;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistDaoTest {

    private PlaylistDao playlistDao;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        playlistDao = new PlaylistDao();
        userDao = new UserDao();
    }

    @Test
    void testCreatePlaylistSuccess() {
        // create user first (FK dependency)
        User user = new User();
        user.setEmail("playlist_test@gmail.com");
        user.setPassword("1234");
        user.setRole("USER");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail("playlist_test@gmail.com");

        Playlist playlist = new Playlist();
        playlist.setUserId(userId);
        playlist.setName("My Playlist");
        playlist.setDescription("Test playlist");
        playlist.setPrivacy("PRIVATE");

        boolean result = playlistDao.createPlaylist(playlist);
        assertTrue(result);

        // cleanup
        playlistDao.deletePlaylistByName("My Playlist", userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testGetUserPlaylists() {
        User user = new User();
        user.setEmail("playlist_fetch@gmail.com");
        user.setPassword("1234");
        user.setRole("USER");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail("playlist_fetch@gmail.com");

        Playlist playlist = new Playlist();
        playlist.setUserId(userId);
        playlist.setName("Fetch Playlist");
        playlist.setDescription("Fetch test");
        playlist.setPrivacy("PUBLIC");

        playlistDao.createPlaylist(playlist);
        var playlists = playlistDao.getPlaylistsByUser(userId);

        assertNotNull(playlists);
        assertFalse(playlists.isEmpty());

        // cleanup
        playlistDao.deletePlaylistByName("Fetch Playlist", userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testDeletePlaylist() {
        User user = new User();
        user.setEmail("playlist_delete@gmail.com");
        user.setPassword("1234");
        user.setRole("USER");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail("playlist_delete@gmail.com");

        Playlist playlist = new Playlist();
        playlist.setUserId(userId);
        playlist.setName("Delete Playlist");
        playlist.setDescription("Delete test");
        playlist.setPrivacy("PRIVATE");

        playlistDao.createPlaylist(playlist);

        boolean deleted = playlistDao.deletePlaylistByName("Delete Playlist", userId);
        assertTrue(deleted);
        userDao.deleteUserById(userId);
    }


}
