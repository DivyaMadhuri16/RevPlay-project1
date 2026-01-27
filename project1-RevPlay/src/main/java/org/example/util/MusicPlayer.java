package org.example.util;

import org.example.model.Song;
import java.util.List;

public class MusicPlayer {
    private List<Song> queue;
    private int currentIndex = 0;
    private boolean isPlaying = false;
    private boolean repeat = false;

    public MusicPlayer(List<Song> queue) {
        this.queue = queue;
    }

    public Song play() {
        isPlaying = true;
        return queue.get(currentIndex);
    }

    public Song pause() {
        isPlaying = false;
        return queue.get(currentIndex);
    }

    public Song next() {
        if (!repeat) {
            currentIndex = (currentIndex + 1) % queue.size();
        }
        isPlaying = true;
        return queue.get(currentIndex);
    }

    public void toggleRepeat() {
        repeat = !repeat;
    }

    public Song currentSong() {
        return queue.get(currentIndex);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isRepeat() {
        return repeat;
    }
}
