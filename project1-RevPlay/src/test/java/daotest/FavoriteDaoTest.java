package daotest;

import org.example.dao.*;
import org.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FavoriteDaoTest {

    private UserDao userDao;
    private ArtistDao artistDao;
    private SongDao songDao;
    private FavoriteDao favoriteDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        artistDao = new ArtistDao();
        songDao = new SongDao();
        favoriteDao = new FavoriteDao();
    }

    @Test
    void testAddFavorite() {
        String email = "fav_" + System.currentTimeMillis() + "@gmail.com";

        // USER
        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("USER");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        // ARTIST USER
        String artistEmail = "artist_" + System.currentTimeMillis() + "@gmail.com";
        User artistUser = new User();
        artistUser.setEmail(artistEmail);
        artistUser.setPassword("1234");
        artistUser.setRole("ARTIST");
        artistUser.setStatus("ACTIVE");

        userDao.registerUser(artistUser);
        int artistUserId = userDao.getUserIdByEmail(artistEmail);

        // ARTIST
        Artist artist = new Artist();
        artist.setUserId(artistUserId);
        artist.setArtistName("Fav Artist");
        artist.setBio("bio");
        artist.setGenre("Pop");
        artist.setSocialLinks("insta");

        artistDao.createArtistProfile(artist);
        int artistId = artistDao.getArtistIdByUserId(artistUserId);

        // SONG
        Song song = new Song();
        song.setArtistId(artistId);
        song.setTitle("Favorite Song");
        song.setGenre("Pop");
        song.setDuration(180);
        song.setReleaseDate(LocalDate.now());

        songDao.uploadSong(song);
        int songId = songDao.getAnySongId();

        // ADD FAVORITE
        assertTrue(favoriteDao.addFavorite(userId, songId));

        // CLEANUP
        favoriteDao.removeFavorite(userId, songId);
        songDao.deleteSongByTitle("Favorite Song", artistId);
        artistDao.deleteArtistByUserId(artistUserId);
        userDao.deleteUserById(artistUserId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testGetFavorites() {
        String email = "fav_list_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("USER");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        // ARTIST USER
        String artistEmail = "artist_list_" + System.currentTimeMillis() + "@gmail.com";
        User artistUser = new User();
        artistUser.setEmail(artistEmail);
        artistUser.setPassword("1234");
        artistUser.setRole("ARTIST");
        artistUser.setStatus("ACTIVE");

        userDao.registerUser(artistUser);
        int artistUserId = userDao.getUserIdByEmail(artistEmail);

        Artist artist = new Artist();
        artist.setUserId(artistUserId);
        artist.setArtistName("Fav Artist 2");
        artist.setBio("bio");
        artist.setGenre("Rock");
        artist.setSocialLinks("fb");

        artistDao.createArtistProfile(artist);
        int artistId = artistDao.getArtistIdByUserId(artistUserId);

        Song song = new Song();
        song.setArtistId(artistId);
        song.setTitle("List Song");
        song.setGenre("Rock");
        song.setDuration(200);
        song.setReleaseDate(LocalDate.now());

        songDao.uploadSong(song);
        int songId = songDao.getAnySongId();

        favoriteDao.addFavorite(userId, songId);

        // GET FAVORITES
        List<Song> favorites = favoriteDao.getFavorites(userId);
        assertNotNull(favorites);
        assertFalse(favorites.isEmpty());

        // CLEANUP
        favoriteDao.removeFavorite(userId, songId);
        songDao.deleteSongByTitle("List Song", artistId);
        artistDao.deleteArtistByUserId(artistUserId);
        userDao.deleteUserById(artistUserId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testRemoveFavorite() {
        String email = "fav_remove_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("USER");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        // ARTIST USER
        String artistEmail = "artist_remove_" + System.currentTimeMillis() + "@gmail.com";
        User artistUser = new User();
        artistUser.setEmail(artistEmail);
        artistUser.setPassword("1234");
        artistUser.setRole("ARTIST");
        artistUser.setStatus("ACTIVE");

        userDao.registerUser(artistUser);
        int artistUserId = userDao.getUserIdByEmail(artistEmail);

        Artist artist = new Artist();
        artist.setUserId(artistUserId);
        artist.setArtistName("Fav Artist 3");
        artist.setBio("bio");
        artist.setGenre("Jazz");
        artist.setSocialLinks("yt");

        artistDao.createArtistProfile(artist);
        int artistId = artistDao.getArtistIdByUserId(artistUserId);

        Song song = new Song();
        song.setArtistId(artistId);
        song.setTitle("Remove Song");
        song.setGenre("Jazz");
        song.setDuration(210);
        song.setReleaseDate(LocalDate.now());

        songDao.uploadSong(song);
        int songId = songDao.getAnySongId();

        favoriteDao.addFavorite(userId, songId);

        // REMOVE FAVORITE
        assertTrue(favoriteDao.removeFavorite(userId, songId));

        // CLEANUP
        songDao.deleteSongByTitle("Remove Song", artistId);
        artistDao.deleteArtistByUserId(artistUserId);
        userDao.deleteUserById(artistUserId);
        userDao.deleteUserById(userId);
    }
}
