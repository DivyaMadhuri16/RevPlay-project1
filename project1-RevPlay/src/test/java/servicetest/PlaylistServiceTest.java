package servicetest;

import org.example.dao.PlaylistDao;
import org.example.dao.PlaylistSongDao;
import org.example.model.Playlist;
import org.example.service.PlaylistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTest {

    @Mock
    private PlaylistDao playlistDao;

    @Mock
    private PlaylistSongDao playlistSongDao;

    @InjectMocks
    private PlaylistService playlistService;

    // ---------------- CREATE PLAYLIST ----------------

    @Test
    void testCreatePlaylistSuccess() {
        Playlist playlist = new Playlist();
        playlist.setUserId(1);
        playlist.setName("My Playlist");
        playlist.setPrivacy("PUBLIC");

        when(playlistDao.playlistExists(1, "My Playlist"))
                .thenReturn(false);
        when(playlistDao.createPlaylist(playlist))
                .thenReturn(true);

        boolean result = playlistService.createPlaylist(playlist);

        assertTrue(result);
        verify(playlistDao).createPlaylist(playlist);
    }

    @Test
    void testCreatePlaylistFail_InvalidName() {
        Playlist playlist = new Playlist();
        playlist.setUserId(1);
        playlist.setName("");
        playlist.setPrivacy("PUBLIC");

        boolean result = playlistService.createPlaylist(playlist);

        assertFalse(result);
        verifyNoInteractions(playlistDao);
    }

    @Test
    void testCreatePlaylistFail_InvalidPrivacy() {
        Playlist playlist = new Playlist();
        playlist.setUserId(1);
        playlist.setName("Test");
        playlist.setPrivacy("FRIENDS");

        boolean result = playlistService.createPlaylist(playlist);

        assertFalse(result);
        verifyNoInteractions(playlistDao);
    }

    @Test
    void testCreatePlaylistFail_AlreadyExists() {
        Playlist playlist = new Playlist();
        playlist.setUserId(1);
        playlist.setName("Test");
        playlist.setPrivacy("PRIVATE");

        when(playlistDao.playlistExists(1, "Test"))
                .thenReturn(true);

        boolean result = playlistService.createPlaylist(playlist);

        assertFalse(result);
        verify(playlistDao, never()).createPlaylist(any());
    }

    // ---------------- ADD SONG TO PLAYLIST ----------------

    @Test
    void testAddSongToPlaylistSuccess() {
        int userId = 1;
        int playlistId = 10;
        int songId = 100;

        when(playlistDao.isPlaylistOwnedByUser(playlistId, userId))
                .thenReturn(true);
        when(playlistSongDao.addSongToPlaylist(playlistId, songId))
                .thenReturn(true);

        boolean result = playlistService.addSongToPlaylist(userId, playlistId, songId);

        assertTrue(result);
        verify(playlistSongDao).addSongToPlaylist(playlistId, songId);
    }

    @Test
    void testAddSongToPlaylistFail_NotOwner() {
        when(playlistDao.isPlaylistOwnedByUser(10, 1))
                .thenReturn(false);

        boolean result = playlistService.addSongToPlaylist(1, 10, 100);

        assertFalse(result);
        verifyNoInteractions(playlistSongDao);
    }

    // ---------------- REMOVE SONG FROM PLAYLIST ----------------

    @Test
    void testRemoveSongFromPlaylistSuccess() {
        when(playlistDao.isPlaylistOwnedByUser(10, 1))
                .thenReturn(true);
        when(playlistSongDao.removeSongFromPlaylist(10, 100))
                .thenReturn(true);

        boolean result = playlistService.removeSongFromPlaylist(1, 10, 100);

        assertTrue(result);
        verify(playlistSongDao).removeSongFromPlaylist(10, 100);
    }

    @Test
    void testRemoveSongFromPlaylistFail_NotOwner() {
        when(playlistDao.isPlaylistOwnedByUser(10, 1))
                .thenReturn(false);

        boolean result = playlistService.removeSongFromPlaylist(1, 10, 100);

        assertFalse(result);
        verifyNoInteractions(playlistSongDao);
    }

    // ---------------- GET SONGS IN PLAYLIST ----------------

    @Test
    void testGetSongsInPlaylist() {
        when(playlistDao.getSongsInPlaylist(10))
                .thenReturn(List.of("Song 1", "Song 2"));

        List<String> songs = playlistService.getSongsInPlaylist(10);

        assertEquals(2, songs.size());
        verify(playlistDao).getSongsInPlaylist(10);
    }

    // ---------------- GET USER PLAYLISTS ----------------

    @Test
    void testGetUserPlaylists() {
        when(playlistDao.getPlaylistsByUser(1))
                .thenReturn(List.of(new Playlist()));

        List<Playlist> playlists = playlistService.getUserPlaylists(1);

        assertFalse(playlists.isEmpty());
        verify(playlistDao).getPlaylistsByUser(1);
    }

    // ---------------- UPDATE PLAYLIST ----------------

    @Test
    void testUpdatePlaylistSuccess() {
        Playlist playlist = new Playlist();
        playlist.setName("Updated");
        playlist.setPrivacy("PRIVATE");

        when(playlistDao.updatePlaylist(playlist))
                .thenReturn(true);

        boolean result = playlistService.updatePlaylist(playlist);

        assertTrue(result);
        verify(playlistDao).updatePlaylist(playlist);
    }

    @Test
    void testUpdatePlaylistFail_InvalidName() {
        Playlist playlist = new Playlist();
        playlist.setName("");
        playlist.setPrivacy("PUBLIC");

        assertFalse(playlistService.updatePlaylist(playlist));
        verifyNoInteractions(playlistDao);
    }

    // ---------------- DELETE PLAYLIST ----------------

    @Test
    void testDeletePlaylist() {
        when(playlistDao.deletePlaylist(10, 1))
                .thenReturn(true);

        boolean result = playlistService.deletePlaylist(10, 1);

        assertTrue(result);
        verify(playlistDao).deletePlaylist(10, 1);
    }

    // ---------------- VIEW PUBLIC PLAYLISTS ----------------

    @Test
    void testViewPublicPlaylists() {
        when(playlistDao.getPublicPlaylists(1))
                .thenReturn(List.of("Public Playlist"));

        List<String> playlists = playlistService.viewPublicPlaylists(1);

        assertEquals(1, playlists.size());
        verify(playlistDao).getPublicPlaylists(1);
    }
}
