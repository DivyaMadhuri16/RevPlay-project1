package org.example.service;

import org.example.dao.AlbumDao;
import org.example.model.Album;

import java.util.List;

public class AlbumService {
    private AlbumDao albumDao = new AlbumDao();
    public List<Album> getAlbumsByArtist(int artistId) {
        return albumDao.getAlbumsByArtist(artistId);
    }

    public boolean createAlbum(Album album) {
        if (album.getAlbumTitle() == null ||
                album.getAlbumTitle().isBlank()) {
            return false;
        }
        return albumDao.createAlbum(album);
    }

    public boolean updateAlbum(Album album) {
        return albumDao.updateAlbum(album);
    }

}
