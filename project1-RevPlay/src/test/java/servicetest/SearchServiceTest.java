package servicetest;

import org.example.dao.AlbumDao;
import org.example.dao.ArtistDao;
import org.example.dao.SongDao;
import org.example.model.Album;
import org.example.model.Artist;
import org.example.model.Song;
import org.example.service.SearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private SongDao songDao;

    @Mock
    private ArtistDao artistDao;

    @Mock
    private AlbumDao albumDao;

    @InjectMocks
    private SearchService searchService;

    // ---------------- SONG SEARCH ----------------

    @Test
    void testSearchSongs() {
        Song song = new Song();
        song.setSongId(1);
        song.setTitle("Hello");

        when(songDao.searchSongs("Hello"))
                .thenReturn(List.of(song));

        List<Song> result = searchService.searchSongs("Hello");

        assertEquals(1, result.size());
        assertEquals("Hello", result.get(0).getTitle());
        verify(songDao).searchSongs("Hello");
    }

    // ---------------- ARTIST SEARCH ----------------

    @Test
    void testSearchArtists() {
        Artist artist = new Artist();
        artist.setArtistId(1);
        artist.setArtistName("Adele");

        when(artistDao.searchArtists("Adele"))
                .thenReturn(List.of(artist));

        List<Artist> result = searchService.searchArtists("Adele");

        assertEquals(1, result.size());
        assertEquals("Adele", result.get(0).getArtistName());
        verify(artistDao).searchArtists("Adele");
    }

    // ---------------- ALBUM SEARCH ----------------

    @Test
    void testSearchAlbums() {
        Album album = new Album();
        album.setAlbumId(1);
        album.setAlbumTitle("30");

        when(albumDao.searchAlbums("30"))
                .thenReturn(List.of(album));

        List<Album> result = searchService.searchAlbums("30");

        assertEquals(1, result.size());
        assertEquals("30", result.get(0).getAlbumTitle());
        verify(albumDao).searchAlbums("30");
    }
}
