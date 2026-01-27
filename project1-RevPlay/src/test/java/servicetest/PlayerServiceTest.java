package servicetest;

import org.example.model.Song;
import org.example.service.PlayerService;
import org.example.service.SongService;
import org.example.service.ListeningHistoryService;
import org.example.util.MusicPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private MusicPlayer musicPlayer;

    @Mock
    private SongService songService;

    @Mock
    private ListeningHistoryService historyService;

    @InjectMocks
    private PlayerService playerService;

    private Song song1;
    private Song song2;

    @BeforeEach
    void setup() {
        song1 = new Song();
        song1.setSongId(1);
        song1.setTitle("Song One");

        song2 = new Song();
        song2.setSongId(2);
        song2.setTitle("Song Two");
    }

    // ---------------- LOAD QUEUE ----------------

    @Test
    void testLoadQueue() {
        List<Song> songs = List.of(song1, song2);

        // Just ensure no exception
        playerService.loadQueue(songs);
    }

    // ---------------- PLAY ----------------

    @Test
    void testPlaySong() {
        int userId = 10;

        when(musicPlayer.play()).thenReturn(song1);

        Song result = playerService.play(userId);

        assertNotNull(result);
        assertEquals(1, result.getSongId());

        verify(historyService).saveHistory(userId, 1);
        verify(songService).incrementPlayCount(1);
    }

    // ---------------- PAUSE ----------------

    @Test
    void testPause() {
        when(musicPlayer.pause()).thenReturn(song1);

        Song result = playerService.pause();

        assertEquals(song1, result);
        verify(musicPlayer).pause();
    }

    // ---------------- NEXT ----------------

    @Test
    void testNextSong() {
        int userId = 20;

        when(musicPlayer.next()).thenReturn(song2);

        Song result = playerService.next(userId);

        assertEquals(2, result.getSongId());

        verify(historyService).saveHistory(userId, 2);
        verify(songService).incrementPlayCount(2);
    }

    // ---------------- TOGGLE REPEAT ----------------

    @Test
    void testToggleRepeat() {
        playerService.toggleRepeat();

        verify(musicPlayer).toggleRepeat();
    }
}
