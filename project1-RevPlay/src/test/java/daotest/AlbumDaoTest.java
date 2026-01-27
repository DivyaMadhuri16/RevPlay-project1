package daotest;

import org.example.dao.AlbumDao;
import org.example.dao.ArtistDao;
import org.example.dao.UserDao;
import org.example.model.Album;
import org.example.model.Artist;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlbumDaoTest {

    private UserDao userDao;
    private ArtistDao artistDao;
    private AlbumDao albumDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        artistDao = new ArtistDao();
        albumDao = new AlbumDao();
    }

    @Test
    void testCreateAlbum() {
        String email = "album_" + System.currentTimeMillis() + "@gmail.com";

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
        artist.setArtistName("Album Artist 1");
        artist.setBio("bio");
        artist.setGenre("Pop");
        artist.setSocialLinks("insta");

        assertTrue(artistDao.createArtistProfile(artist));
        int artistId = artistDao.getArtistIdByUserId(userId);

        // ALBUM
        Album album = new Album();
        album.setArtistId(artistId);
        album.setAlbumTitle("JUnit Album");
        album.setReleaseDate(LocalDate.now());

        assertTrue(albumDao.createAlbum(album));

        // CLEANUP
        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testGetAlbumsByArtist() {
        String email = "album_fetch_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Album Artist 2");
        artist.setBio("bio");
        artist.setGenre("Rock");
        artist.setSocialLinks("fb");

        artistDao.createArtistProfile(artist);
        int artistId = artistDao.getArtistIdByUserId(userId);

        Album album = new Album();
        album.setArtistId(artistId);
        album.setAlbumTitle("Fetch Album");
        album.setReleaseDate(LocalDate.now());

        albumDao.createAlbum(album);

        List<Album> albums = albumDao.getAlbumsByArtist(artistId);
        assertNotNull(albums);
        assertFalse(albums.isEmpty());

        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testUpdateAlbum() {
        String email = "album_update_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Album Artist 3");
        artist.setBio("bio");
        artist.setGenre("Jazz");
        artist.setSocialLinks("yt");

        artistDao.createArtistProfile(artist);
        int artistId = artistDao.getArtistIdByUserId(userId);

        Album album = new Album();
        album.setArtistId(artistId);
        album.setAlbumTitle("Old Album");
        album.setReleaseDate(LocalDate.now());

        albumDao.createAlbum(album);

        // fetch album id
        List<Album> albums = albumDao.getAlbumsByArtist(artistId);
        int albumId = albums.get(0).getAlbumId();

        album.setAlbumId(albumId);
        album.setAlbumTitle("Updated Album");
        album.setReleaseDate(LocalDate.now());

        assertTrue(albumDao.updateAlbum(album));

        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testSearchAlbums() {
        String email = "album_search_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Album Artist 4");
        artist.setBio("bio");
        artist.setGenre("Classical");
        artist.setSocialLinks("x");

        artistDao.createArtistProfile(artist);
        int artistId = artistDao.getArtistIdByUserId(userId);

        Album album = new Album();
        album.setArtistId(artistId);
        album.setAlbumTitle("Love Hits");
        album.setReleaseDate(LocalDate.now());

        albumDao.createAlbum(album);

        List<Album> albums = albumDao.searchAlbums("Love");
        assertNotNull(albums);
        assertFalse(albums.isEmpty());

        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }
}
