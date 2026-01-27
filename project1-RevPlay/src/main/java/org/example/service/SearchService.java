package org.example.service;

import java.util.List;

import org.example.dao.AlbumDao;
import org.example.dao.ArtistDao;
import org.example.dao.SongDao;
import org.example.model.*;

public class SearchService {
    private SongDao songDao = new SongDao();
    private ArtistDao artistDao = new ArtistDao();
    private AlbumDao albumDao = new AlbumDao();

    public List<Song> searchSongs(String keyword) {
        return songDao.searchSongs(keyword);
    }

    public List<Artist> searchArtists(String keyword) {
        return artistDao.searchArtists(keyword);
    }

    public List<Album> searchAlbums(String keyword) {
        return albumDao.searchAlbums(keyword);
    }
}
