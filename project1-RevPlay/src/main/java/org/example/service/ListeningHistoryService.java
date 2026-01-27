package org.example.service;

import org.example.dao.ListeningHistoryDao;
import java.util.List;

public class ListeningHistoryService {
    private ListeningHistoryDao dao = new ListeningHistoryDao();

    public List<String> viewHistory(int userId) {
        return dao.getListeningHistory(userId);
    }

    public void saveHistory(int userId, int songId) {
        dao.saveListeningHistory(userId, songId);
    }

    public List<String> viewRecentlyPlayed(int userId) {
        return dao.getRecentlyPlayed(userId, 4); // last 4 songs
    }

}
