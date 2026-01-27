package servicetest;

import org.example.dao.FavoriteDao;
import org.example.model.Song;
import org.example.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteDao favoriteDao;   // mocked DAO

    @InjectMocks
    private FavoriteService favoriteService; // real service

    // ---------------- ADD FAVORITE ----------------

    @Test
    void testAddFavorite() {
        int userId = 1;
        int songId = 10;

        when(favoriteDao.addFavorite(userId, songId))
                .thenReturn(true);

        boolean result = favoriteService.addFavorite(userId, songId);

        assertTrue(result);
        verify(favoriteDao).addFavorite(userId, songId);
    }

    // ---------------- REMOVE FAVORITE ----------------

    @Test
    void testRemoveFavorite() {
        int userId = 1;
        int songId = 10;

        when(favoriteDao.removeFavorite(userId, songId))
                .thenReturn(true);

        boolean result = favoriteService.removeFavorite(userId, songId);

        assertTrue(result);
        verify(favoriteDao).removeFavorite(userId, songId);
    }

    // ---------------- VIEW FAVORITES ----------------

    @Test
    void testViewFavorites() {
        int userId = 1;

        Song song = new Song();
        song.setSongId(10);
        song.setTitle("Test Song");

        when(favoriteDao.getFavorites(userId))
                .thenReturn(List.of(song));

        List<Song> favorites = favoriteService.viewFavorites(userId);

        assertNotNull(favorites);
        assertEquals(1, favorites.size());
        assertEquals("Test Song", favorites.get(0).getTitle());

        verify(favoriteDao).getFavorites(userId);
    }
}
