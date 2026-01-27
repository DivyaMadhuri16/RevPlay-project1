package org.example.service;

import java.util.List;
import org.example.dao.FavoriteDao;
import org.example.model.Song;

public class FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDao();

    public boolean addFavorite(int userId, int songId) {
        return favoriteDao.addFavorite(userId, songId);
    }

    public boolean removeFavorite(int userId, int songId) {
        return favoriteDao.removeFavorite(userId, songId);
    }

    public List<Song> viewFavorites(int userId) {
        return favoriteDao.getFavorites(userId);
    }
}

