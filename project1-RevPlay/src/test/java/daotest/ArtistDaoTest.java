package daotest;

import org.example.dao.ArtistDao;
import org.example.dao.UserDao;
import org.example.model.Artist;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArtistDaoTest {

    private UserDao userDao;
    private ArtistDao artistDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        artistDao = new ArtistDao();
    }

    @Test
    void testCreateArtistProfile() {
        String email = "artist_" + System.currentTimeMillis() + "@gmail.com";

        // USER
        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        // ARTIST
        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("JUnit Artist");
        artist.setBio("Test bio");
        artist.setGenre("Pop");
        artist.setSocialLinks("insta");

        boolean created = artistDao.createArtistProfile(artist);
        assertTrue(created);

        // CLEANUP
        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testArtistProfileExists() {
        String email = "artist_exists_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Exists Artist");
        artist.setBio("bio");
        artist.setGenre("Rock");
        artist.setSocialLinks("fb");

        artistDao.createArtistProfile(artist);

        assertTrue(artistDao.artistProfileExists(userId));

        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testGetArtistByUserId() {
        String email = "artist_get_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Fetch Artist");
        artist.setBio("fetch bio");
        artist.setGenre("Jazz");
        artist.setSocialLinks("yt");

        artistDao.createArtistProfile(artist);

        Artist fetchedArtist = artistDao.getArtistByUserId(userId);
        assertNotNull(fetchedArtist);
        assertEquals("Fetch Artist", fetchedArtist.getArtistName());

        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testUpdateArtistProfile() {
        String email = "artist_update_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Old Name");
        artist.setBio("old bio");
        artist.setGenre("Classical");
        artist.setSocialLinks("x");

        artistDao.createArtistProfile(artist);

        artist.setArtistName("Updated Name");
        artist.setBio("updated bio");
        artist.setGenre("Fusion");

        boolean updated = artistDao.updateArtistProfile(artist);
        assertTrue(updated);

        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testSearchArtists() {
        String email = "artist_search_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Rock Star");
        artist.setBio("bio");
        artist.setGenre("Rock");
        artist.setSocialLinks("snap");

        artistDao.createArtistProfile(artist);

        List<Artist> artists = artistDao.searchArtists("Rock");
        assertNotNull(artists);
        assertFalse(artists.isEmpty());

        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testDeleteArtistByUserId() {
        String email = "artist_delete_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Delete Artist");
        artist.setBio("bio");
        artist.setGenre("HipHop");
        artist.setSocialLinks("insta");

        artistDao.createArtistProfile(artist);

        boolean deleted = artistDao.deleteArtistByUserId(userId);
        assertTrue(deleted);

        userDao.deleteUserById(userId);
    }
}
