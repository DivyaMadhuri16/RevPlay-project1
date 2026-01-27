package daotest;

import org.example.dao.UserDao;
import org.example.dao.ArtistDao;
import org.example.dao.SongDao;
import org.example.model.User;
import org.example.model.Artist;
import org.example.model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SongDaoTest {

    private UserDao userDao;
    private ArtistDao artistDao;
    private SongDao songDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        artistDao = new ArtistDao();
        songDao = new SongDao();
    }

    @Test
    void testUploadSong() {
        String email = "song_" + System.currentTimeMillis() + "@gmail.com";
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
        artist.setArtistName("Song Artist 1");
        artist.setBio("bio");
        artist.setGenre("Pop");
        artist.setSocialLinks("insta");

        assertTrue(artistDao.createArtistProfile(artist));
        int artistId = artistDao.getArtistIdByUserId(userId);

        // SONG
        Song song = new Song();
        song.setArtistId(artistId);
        song.setTitle("JUnit Song");
        song.setGenre("Pop");
        song.setDuration(180);
        song.setReleaseDate(LocalDate.now());

        assertTrue(songDao.uploadSong(song));

        // CLEANUP
        songDao.deleteSongByTitle("JUnit Song", artistId);
        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testGetSongsByArtist() {
        String email = "song_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Song Artist 2");
        artist.setBio("bio");
        artist.setGenre("Rock");
        artist.setSocialLinks("fb");

        artistDao.createArtistProfile(artist);
        int artistId = artistDao.getArtistIdByUserId(userId);

        Song song = new Song();
        song.setArtistId(artistId);
        song.setTitle("Fetch Song");
        song.setGenre("Rock");
        song.setDuration(200);
        song.setReleaseDate(LocalDate.now());

        songDao.uploadSong(song);

        List<Song> songs = songDao.getSongsByArtist(artistId);
        assertNotNull(songs);
        assertFalse(songs.isEmpty());

        songDao.deleteSongByTitle("Fetch Song", artistId);
        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testUpdateSong() {
        String email = "song_update_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Song Artist 3");
        artist.setBio("bio");
        artist.setGenre("Jazz");
        artist.setSocialLinks("yt");

        artistDao.createArtistProfile(artist);
        int artistId = artistDao.getArtistIdByUserId(userId);

        Song song = new Song();
        song.setArtistId(artistId);
        song.setTitle("Old Song");
        song.setGenre("Jazz");
        song.setDuration(150);
        song.setReleaseDate(LocalDate.now());

        assertTrue(songDao.uploadSong(song));
        List<Song> songs = songDao.getSongsByArtist(artistId);
        int songId = songs.get(0).getSongId();


        song.setSongId(songId);
        song.setTitle("Updated Song");
        song.setGenre("Melody");
        song.setDuration(220);

        boolean updated = songDao.updateSong(song);
        assertTrue(updated);

        songDao.deleteSongByTitle("Updated Song", artistId);
        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testIncrementPlayCount() {
        String email = "song_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Song Artist 4");
        artist.setBio("bio");
        artist.setGenre("Classic");
        artist.setSocialLinks("x");

        artistDao.createArtistProfile(artist);
        int artistId = artistDao.getArtistIdByUserId(userId);

        Song song = new Song();
        song.setArtistId(artistId);
        song.setTitle("Play Song");
        song.setGenre("Classic");
        song.setDuration(210);
        song.setReleaseDate(LocalDate.now());

        songDao.uploadSong(song);
        int songId = songDao.getAnySongId();



        songDao.incrementPlayCount(songId);

        List<Song> songs = songDao.getSongsWithPlayCount(artistId);
        assertFalse(songs.isEmpty());

        songDao.deleteSongByTitle("Play Song", artistId);
        artistDao.deleteArtistByUserId(userId);
        userDao.deleteUserById(userId);
    }

    @Test
    void testDeleteSongByTitle() {
        String email = "song_" + System.currentTimeMillis() + "@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRole("ARTIST");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        int userId = userDao.getUserIdByEmail(email);

        Artist artist = new Artist();
        artist.setUserId(userId);
        artist.setArtistName("Song Artist 5");
        artist.setBio("bio");
        artist.setGenre("HipHop");
        artist.setSocialLinks("snap");

        artistDao.createArtistProfile(artist);
        int artistId = artistDao.getArtistIdByUserId(userId);

        Song song = new Song();
        song.setArtistId(artistId);
    }
}