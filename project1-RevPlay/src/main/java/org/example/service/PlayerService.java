package org.example.service;

import org.example.model.PlayHistory;
import org.example.model.Song;
import org.example.util.MusicPlayer;
import java.time.LocalDateTime;
import java.util.*;

public class PlayerService {
    private MusicPlayer player;
    private Map<Integer, List<PlayHistory>> historyMap = new HashMap<>();
    private ListeningHistoryService historyService = new ListeningHistoryService();
    private SongService songService = new SongService();

    public void loadQueue(List<Song> songs) {
        player = new MusicPlayer(songs);
    }

    public Song play(int userId) {
        Song song = player.play();
        historyService.saveHistory(userId, song.getSongId());
        songService.incrementPlayCount(song.getSongId());
        return song;
    }

    public Song pause() {
        return player.pause();
    }

    public Song next(int userId) {
        Song song = player.next();
        historyService.saveHistory(userId, song.getSongId());
        songService.incrementPlayCount(song.getSongId());
        return song;
    }

    public void toggleRepeat() {
        player.toggleRepeat();
    }

    public List<PlayHistory> getHistory(int userId) {
        return historyMap.getOrDefault(userId, new ArrayList<>());
    }

    private void saveHistory(int userId, int songId) {
        PlayHistory ph = new PlayHistory(userId, songId, LocalDateTime.now());

        historyMap
                .computeIfAbsent(userId, k -> new ArrayList<>())
                .add(ph);
    }
}
