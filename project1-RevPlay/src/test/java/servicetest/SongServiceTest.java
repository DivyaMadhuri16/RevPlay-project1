package servicetest;

import org.example.dao.SongDao;
import org.example.model.Song;
import org.example.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

    @Mock
    private SongDao songDao;   // mocked DAO

    @InjectMocks
    private SongService songService; // real service

    private Song song;

    @BeforeEach
    void setUp() {
        song = new Song();
        song.setSongId(1);
        song.setArtistId(10);
        song.setTitle("Test Song");
        song.setGenre("Pop");
        song.setDuration(200);
    }

    // ---------------- GET SONGS BY ARTIST ----------------

    @Test
    void testGetSongsByArtist() {
        when(songDao.getSongsByArtist(10))
                .thenReturn(List.of(song));

        List<Song> songs = songService.getSongsByArtist(10);

        assertNotNull(songs);
        assertEquals(1, songs.size());
        verify(songDao).getSongsByArtist(10);
    }

    // ---------------- UPLOAD SONG ----------------

    @Test
    void testUploadSongSuccess() {
        when(songDao.uploadSong(song))
                .thenReturn(true);

        boolean result = songService.uploadSong(song);

        assertTrue(result);
        verify(songDao).uploadSong(song);
    }

    @Test
    void testUploadSongFailWhenTitleNull() {
        song.setTitle(null);

        boolean result = songService.uploadSong(song);

        assertFalse(result);
        verify(songDao, never()).uploadSong(any());
    }

    @Test
    void testUploadSongFailWhenTitleBlank() {
        song.setTitle("   ");

        boolean result = songService.uploadSong(song);

        assertFalse(result);
        verify(songDao, never()).uploadSong(any());
    }

    // ---------------- UPDATE SONG ----------------

    @Test
    void testUpdateSong() {
        when(songDao.updateSong(song))
                .thenReturn(true);

        boolean result = songService.updateSong(song);

        assertTrue(result);
        verify(songDao).updateSong(song);
    }

    // ---------------- DELETE SONG ----------------

    @Test
    void testDeleteSong() {
        when(songDao.deleteSong(1, 10))
                .thenReturn(true);

        boolean result = songService.deleteSong(1, 10);

        assertTrue(result);
        verify(songDao).deleteSong(1, 10);
    }

    // ---------------- PLAY COUNT ----------------

    @Test
    void testIncrementPlayCount() {
        songService.incrementPlayCount(1);

        verify(songDao).incrementPlayCount(1);
    }

    // ---------------- SONGS WITH PLAY COUNT ----------------

    @Test
    void testGetSongsWithPlayCount() {
        when(songDao.getSongsWithPlayCount(10))
                .thenReturn(List.of(song));

        List<Song> songs = songService.getSongsWithPlayCount(10);

        assertFalse(songs.isEmpty());
        verify(songDao).getSongsWithPlayCount(10);
    }

    // ---------------- FAVORITE ANALYTICS ----------------

    @Test
    void testGetArtistFavoritesAnalytics() {
        when(songDao.getUsersWhoFavoritedArtistSongs(10))
                .thenReturn(Arrays.asList("user1@gmail.com", "user2@gmail.com"));

        List<String> users = songService.getArtistFavoritesAnalytics(10);

        assertEquals(2, users.size());
        verify(songDao).getUsersWhoFavoritedArtistSongs(10);
    }

    // ---------------- GET ALL SONGS ----------------

    @Test
    void testGetAllSongs() {
        when(songDao.getAllSongs())
                .thenReturn(List.of(song));

        List<Song> songs = songService.getAllSongs();

        assertEquals(1, songs.size());
        verify(songDao).getAllSongs();
    }

    // ---------------- ADD SONG TO ALBUM ----------------

    @Test
    void testAddSongToAlbum() {
        when(songDao.addSongToAlbum(1, 5))
                .thenReturn(true);

        boolean result = songService.addSongToAlbum(1, 5);

        assertTrue(result);
        verify(songDao).addSongToAlbum(1, 5);
    }

    // ---------------- BROWSE METHODS ----------------

    @Test
    void testBrowseByGenre() {
        when(songDao.browseSongsByGenre("Pop"))
                .thenReturn(List.of(song));

        List<Song> songs = songService.browseByGenre("Pop");

        assertFalse(songs.isEmpty());
        verify(songDao).browseSongsByGenre("Pop");
    }

    @Test
    void testBrowseByArtist() {
        when(songDao.browseSongsByArtist(10))
                .thenReturn(List.of(song));

        List<Song> songs = songService.browseByArtist(10);

        assertFalse(songs.isEmpty());
        verify(songDao).browseSongsByArtist(10);
    }

    @Test
    void testBrowseByAlbum() {
        when(songDao.browseSongsByAlbum(5))
                .thenReturn(List.of(song));

        List<Song> songs = songService.browseByAlbum(5);

        assertFalse(songs.isEmpty());
        verify(songDao).browseSongsByAlbum(5);
    }
}
