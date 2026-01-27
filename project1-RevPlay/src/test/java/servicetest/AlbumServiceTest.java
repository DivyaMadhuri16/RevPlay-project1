package servicetest;

import org.example.dao.AlbumDao;
import org.example.model.Album;
import org.example.service.AlbumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

    @Mock
    private AlbumDao albumDao;   // mocked DAO

    @InjectMocks
    private AlbumService albumService; // real service

    private Album album;

    @BeforeEach
    void setUp() {
        album = new Album();
        album.setAlbumId(1);
        album.setArtistId(10);
        album.setAlbumTitle("Test Album");
        album.setReleaseDate(LocalDate.now());
    }

    // ---------------- GET ALBUMS BY ARTIST ----------------

    @Test
    void testGetAlbumsByArtist() {
        when(albumDao.getAlbumsByArtist(10))
                .thenReturn(List.of(album));

        List<Album> albums = albumService.getAlbumsByArtist(10);

        assertNotNull(albums);
        assertEquals(1, albums.size());
        verify(albumDao).getAlbumsByArtist(10);
    }

    // ---------------- CREATE ALBUM ----------------

    @Test
    void testCreateAlbumSuccess() {
        when(albumDao.createAlbum(album))
                .thenReturn(true);

        boolean result = albumService.createAlbum(album);

        assertTrue(result);
        verify(albumDao).createAlbum(album);
    }

    @Test
    void testCreateAlbumFailWhenTitleNull() {
        album.setAlbumTitle(null);

        boolean result = albumService.createAlbum(album);

        assertFalse(result);
        verify(albumDao, never()).createAlbum(any());
    }

    @Test
    void testCreateAlbumFailWhenTitleBlank() {
        album.setAlbumTitle("   ");

        boolean result = albumService.createAlbum(album);

        assertFalse(result);
        verify(albumDao, never()).createAlbum(any());
    }

    // ---------------- UPDATE ALBUM ----------------

    @Test
    void testUpdateAlbum() {
        when(albumDao.updateAlbum(album))
                .thenReturn(true);

        boolean result = albumService.updateAlbum(album);

        assertTrue(result);
        verify(albumDao).updateAlbum(album);
    }
}
