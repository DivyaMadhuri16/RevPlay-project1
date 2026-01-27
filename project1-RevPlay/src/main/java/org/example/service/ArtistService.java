package org.example.service;

import org.example.dao.ArtistDao;
import org.example.model.Artist;

public class ArtistService {
    private ArtistDao artistDao = new ArtistDao();

    public boolean createArtistProfile(Artist artist) {
        if (artistDao.artistProfileExists(artist.getUserId())) {
            return false;
        }
        return artistDao.createArtistProfile(artist);
    }

    public Artist viewArtistProfile(int userId) {
        return artistDao.getArtistByUserId(userId);
    }

    public boolean updateArtistProfile(Artist artist) {
        if (!artistDao.artistProfileExists(artist.getUserId())) {
            return false;
        }
        return artistDao.updateArtistProfile(artist);
    }

    public Integer getArtistIdByUserId(int userId) {
        return artistDao.getArtistIdByUserId(userId);
    }


}