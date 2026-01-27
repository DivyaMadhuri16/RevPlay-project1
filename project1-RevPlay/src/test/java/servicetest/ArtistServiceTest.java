package servicetest;

import org.example.dao.ArtistDao;
import org.example.model.Artist;
import org.example.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @Mock
    private ArtistDao artistDao;   // mocked DAO

    @InjectMocks
    private ArtistService artistService; // real service

    private Artist artist;

    @BeforeEach
    void setUp() {
        artist = new Artist();
        artist.setUserId(1);
        artist.setArtistName("Test Artist");
        artist.setBio("Bio");
        artist.setGenre("Pop");
        artist.setSocialLinks("insta");
    }

    // ---------------- CREATE ARTIST PROFILE ----------------

    @Test
    void testCreateArtistProfileSuccess() {
        when(artistDao.artistProfileExists(artist.getUserId()))
                .thenReturn(false);
        when(artistDao.createArtistProfile(artist))
                .thenReturn(true);

        boolean result = artistService.createArtistProfile(artist);

        assertTrue(result);
        verify(artistDao).createArtistProfile(artist);
    }

    @Test
    void testCreateArtistProfileFailWhenAlreadyExists() {
        when(artistDao.artistProfileExists(artist.getUserId()))
                .thenReturn(true);

        boolean result = artistService.createArtistProfile(artist);

        assertFalse(result);
        verify(artistDao, never()).createArtistProfile(any());
    }

    // ---------------- VIEW ARTIST PROFILE ----------------

    @Test
    void testViewArtistProfileSuccess() {
        when(artistDao.getArtistByUserId(1))
                .thenReturn(artist);

        Artist result = artistService.viewArtistProfile(1);

        assertNotNull(result);
        assertEquals("Test Artist", result.getArtistName());
    }

    @Test
    void testViewArtistProfileNotFound() {
        when(artistDao.getArtistByUserId(1))
                .thenReturn(null);

        Artist result = artistService.viewArtistProfile(1);

        assertNull(result);
    }

    // ---------------- UPDATE ARTIST PROFILE ----------------

    @Test
    void testUpdateArtistProfileSuccess() {
        when(artistDao.artistProfileExists(artist.getUserId()))
                .thenReturn(true);
        when(artistDao.updateArtistProfile(artist))
                .thenReturn(true);

        boolean result = artistService.updateArtistProfile(artist);

        assertTrue(result);
        verify(artistDao).updateArtistProfile(artist);
    }

    @Test
    void testUpdateArtistProfileFailWhenProfileNotExists() {
        when(artistDao.artistProfileExists(artist.getUserId()))
                .thenReturn(false);

        boolean result = artistService.updateArtistProfile(artist);

        assertFalse(result);
        verify(artistDao, never()).updateArtistProfile(any());
    }

    // ---------------- GET ARTIST ID ----------------

    @Test
    void testGetArtistIdByUserId() {
        when(artistDao.getArtistIdByUserId(1))
                .thenReturn(10);

        Integer artistId = artistService.getArtistIdByUserId(1);

        assertEquals(10, artistId);
    }
}
