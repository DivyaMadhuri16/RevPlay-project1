package org.example.controller;

import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Album;
import org.example.model.Artist;
import org.example.model.Song;
import org.example.model.User;
import org.example.service.AlbumService;
import org.example.service.ArtistService;
import org.example.service.SongService;

public class ArtistController {
    private static final Logger logger = LogManager.getLogger(ArtistController.class);
    private ArtistService artistService = new ArtistService();
    private SongService songService = new SongService();
    private AlbumService albumService = new AlbumService();

    private Scanner sc;
    private User loggedInUser;

    public ArtistController(Scanner sc, User loggedInUser) {
        this.sc = sc;
        this.loggedInUser = loggedInUser;
    }

    private Integer getArtistId() {
        Integer artistId = artistService.getArtistIdByUserId(loggedInUser.getUserId());
        if (artistId == null) {
            logger.error("Please create artist profile first");
        }
        return artistId;
    }

    // --- CREATE PROFILE ---
    public void createArtistProfile() {
        if (loggedInUser == null ||
                !"ARTIST".equalsIgnoreCase(loggedInUser.getRole())) {
            logger.error("Only logged-in artists can create profiles");
            return;
        }

        logger.info("Enter Artist Name:");
        sc.nextLine();
        String name = sc.nextLine();

        logger.info("Enter Bio:");
        String bio = sc.nextLine();

        logger.info("Enter Genre:");
        String genre = sc.nextLine();

        logger.info("Enter Social Links:");
        String links = sc.nextLine();

        Artist artist = new Artist();
        artist.setUserId(loggedInUser.getUserId());
        artist.setArtistName(name);
        artist.setBio(bio);
        artist.setGenre(genre);
        artist.setSocialLinks(links);

        boolean created = artistService.createArtistProfile(artist);
        if (created) {
            logger.info("Artist profile created successfully");
        } else {
            logger.error("Artist profile already exists");
        }
    }

    // --- VIEW PROFILE ---
    public void viewArtistProfile() {
        if (loggedInUser == null ||
                !"ARTIST".equalsIgnoreCase(loggedInUser.getRole())) {
            logger.error("Please login as artist first");
            return;
        }

        Artist artist = artistService.viewArtistProfile(loggedInUser.getUserId());
        if (artist == null) {
            logger.error("Artist profile not found. Please create profile first.");
            return;
        }

        logger.info("\n--- Artist Profile ---");
        logger.info("Name   : {}", artist.getArtistName());
        logger.info("Bio    : {}", artist.getBio());
        logger.info("Genre  : {}", artist.getGenre());
        logger.info("Links  : {}", artist.getSocialLinks());
    }

    // --- UPDATE PROFILE ---
    public void updateArtistProfile() {

        if (loggedInUser == null ||
                !"ARTIST".equalsIgnoreCase(loggedInUser.getRole())) {
            logger.error("Please login as artist first");
            return;
        }

        // check if profile exists
        Artist existing = artistService.viewArtistProfile(loggedInUser.getUserId());
        if (existing == null) {
            logger.error("Artist profile not found. Create profile first.");
            return;
        }

        logger.info("Enter Artist Name (current: {}):",
                existing.getArtistName());
        sc.nextLine();
        String name = sc.nextLine();

        logger.info("Enter Bio (current: {}):",
                existing.getBio());
        String bio = sc.nextLine();

        logger.info("Enter Genre (current: {}):",
                existing.getGenre());
        String genre = sc.nextLine();

        logger.info("Enter Social Links (current: {}):",
                existing.getSocialLinks());
        String links = sc.nextLine();

        Artist artist = new Artist();
        artist.setUserId(loggedInUser.getUserId());
        artist.setArtistName(name);
        artist.setBio(bio);
        artist.setGenre(genre);
        artist.setSocialLinks(links);

        boolean updated = artistService.updateArtistProfile(artist);
        if (updated) {
            logger.info("Artist profile updated successfully");
        } else {
            logger.error("Failed to update artist profile");
        }
    }

    // --- UPLOAD SONG ---
    public void uploadSong() {
        if (loggedInUser == null ||
                !"ARTIST".equalsIgnoreCase(loggedInUser.getRole())) {
            logger.error("Only artists can upload songs");
            return;
        }

        Artist artist = artistService.viewArtistProfile(loggedInUser.getUserId());
        if (artist == null) {
            logger.error("No artist profile found for this account. Please create one first.");
            return;
        }

        logger.info("Enter Song Title: ");
        sc.nextLine();
        String title = sc.nextLine();

        logger.info("Enter Genre: ");
        String genre = sc.nextLine();

        logger.info("Enter Duration (in seconds):");
        int duration = Integer.parseInt(sc.next());

        logger.info("Enter Release Date (yyyy-mm-dd):");
        String dateStr = sc.next();

        Song song = new Song();
        song.setArtistId(artist.getArtistId());
        song.setTitle(title);
        song.setGenre(genre);
        song.setDuration(duration);
        song.setReleaseDate(
                java.time.LocalDate.parse(dateStr)
        );

        boolean uploaded = songService.uploadSong(song);
        if (uploaded) {
            logger.info("Song uploaded successfully");
        } else {
            logger.error("Failed to upload song");
        }
    }

    // --- CREATE ALBUM ---
    public void createAlbum() {
        if (loggedInUser == null ||
                !"ARTIST".equalsIgnoreCase(loggedInUser.getRole())) {
            logger.error("Only artists can create albums");
            return;
        }

        Artist artist = artistService.viewArtistProfile(loggedInUser.getUserId());
        if (artist == null) {
            logger.error("Create artist profile before creating albums");
            return;
        }

        logger.info("Enter Album Title:");
        sc.nextLine();
        String name = sc.nextLine();

        logger.info("Enter Release Date (yyyy-mm-dd):");
        String dateStr = sc.next();

        Album album = new Album();
        album.setArtistId(artist.getArtistId());
        album.setAlbumTitle(name);
        album.setReleaseDate(java.time.LocalDate.parse(dateStr));

        boolean created = albumService.createAlbum(album);
        if (created) {
            logger.info("Album created successfully");
        } else {
            logger.error("Failed to create album");
        }
    }

    // --- ADD SONG TO ALBUM ---
    public void addSongToAlbum() {
        if (loggedInUser == null ||
                !"ARTIST".equalsIgnoreCase(loggedInUser.getRole())) {
            logger.error("Only artists can perform this action");
            return;
        }

        Artist artist = artistService.viewArtistProfile(loggedInUser.getUserId());
        if (artist == null) {
            logger.error("Create artist profile first");
            return;
        }

        List<Album> albums = albumService.getAlbumsByArtist(artist.getArtistId());
        if (albums.isEmpty()) {
            logger.error("No albums found. Create an album first.");
            return;
        }

        logger.info("--- Albums ---");
        for (Album a : albums) {
            logger.info("{} : {}", a.getAlbumId(), a.getAlbumTitle());
        }

        logger.info("Enter Album ID:");
        int albumId = Integer.parseInt(sc.next());

        List<Song> songs = songService.getSongsByArtist(artist.getArtistId());
        if (songs.isEmpty()) {
            logger.error("No songs found. Upload songs first.");
            return;
        }

        logger.info("--- Songs ---");
        for (Song s : songs) {
            logger.info("{} : {}", s.getSongId(), s.getTitle());
        }

        logger.info("Enter Song ID to add to album:");
        int songId = Integer.parseInt(sc.next());

        boolean added = songService.addSongToAlbum(songId, albumId);
        if (added) {
            logger.info("Song added to album successfully");
        } else {
            logger.error("Failed to add song to album");
        }
    }

    // --- UPDATE SONG ---
    public void updateSong() {
        if (loggedInUser == null ||
                !"ARTIST".equalsIgnoreCase(loggedInUser.getRole())) {
            logger.error("Only artists can update songs");
            return;
        }

        Artist artist = artistService.viewArtistProfile(loggedInUser.getUserId());
        if (artist == null) {
            logger.error("Create artist profile first");
            return;
        }

        List<Song> songs = songService.getSongsByArtist(artist.getArtistId());
        if (songs.isEmpty()) {
            logger.error("No songs found to update");
            return;
        }

        logger.info("--- Your Songs ---");
        for (Song s : songs) {
            logger.info("{} : {}", s.getSongId(), s.getTitle());
        }

        logger.info("Enter Song ID to update:");
        int songId = Integer.parseInt(sc.next());

        sc.nextLine();
        logger.info("Enter New Title:");
        String title = sc.nextLine();

        logger.info("Enter New Genre:");
        String genre = sc.nextLine();

        logger.info("Enter New Duration (seconds):");
        int duration = Integer.parseInt(sc.next());

        logger.info("Enter New Release Date (yyyy-mm-dd):");
        String dateStr = sc.next();

        Song song = new Song();
        song.setSongId(songId);
        song.setArtistId(artist.getArtistId());
        song.setTitle(title);
        song.setGenre(genre);
        song.setDuration(duration);
        song.setReleaseDate(java.time.LocalDate.parse(dateStr));

        boolean updated = songService.updateSong(song);
        if (updated) {
            logger.info("Song updated successfully");
        } else {
            logger.error("Failed to update song");
        }
    }

    // --- UPDATE ALBUM ---
    public void updateAlbum() {
        if (loggedInUser == null ||
                !"ARTIST".equalsIgnoreCase(loggedInUser.getRole())) {
            logger.error("Only artists can update albums");
            return;
        }

        Artist artist = artistService.viewArtistProfile(loggedInUser.getUserId());
        if (artist == null) {
            logger.error("Create artist profile first");
            return;
        }

        List<Album> albums = albumService.getAlbumsByArtist(artist.getArtistId());
        if (albums.isEmpty()) {
            logger.error("No albums found to update");
            return;
        }

        logger.info("--- Your Albums ---");
        for (Album a : albums) {
            logger.info("{} : {}", a.getAlbumId(), a.getAlbumTitle());
        }

        logger.info("Enter Album ID to update:");
        int albumId = Integer.parseInt(sc.next());

        sc.nextLine();
        logger.info("Enter New Album Title:");
        String title = sc.nextLine();

        logger.info("Enter New Release Date (yyyy-mm-dd):");
        String dateStr = sc.next();

        Album album = new Album();
        album.setAlbumId(albumId);
        album.setArtistId(artist.getArtistId());
        album.setAlbumTitle(title);
        album.setReleaseDate(java.time.LocalDate.parse(dateStr));

        boolean updated = albumService.updateAlbum(album);
        if (updated) {
            logger.info("Album updated successfully");
        } else {
            logger.error("Failed to update album");
        }
    }

    // --- DELETE SONG ---
    public void deleteSong() {
        if (loggedInUser == null ||
                !"ARTIST".equalsIgnoreCase(loggedInUser.getRole())) {
            logger.error("Only artists can delete songs");
            return;
        }

        Artist artist = artistService.viewArtistProfile(loggedInUser.getUserId());
        if (artist == null) {
            logger.error("Create artist profile first");
            return;
        }

        List<Song> songs = songService.getSongsByArtist(artist.getArtistId());
        if (songs.isEmpty()) {
            logger.error("No songs found to delete");
            return;
        }

        logger.info("--- Your Songs ---");
        for (Song s : songs) {
            logger.info("{} : {}", s.getSongId(), s.getTitle());
        }

        logger.info("Enter Song ID to delete:");
        int songId = Integer.parseInt(sc.next());

        boolean deleted = songService.deleteSong(songId, artist.getArtistId());
        if (deleted) {
            logger.info("Song deleted successfully");
        } else {
            logger.error("Failed to delete song");
        }
    }

    // --- VIEW PLAY COUNT ---
    public void viewPlayCount() {
        if (loggedInUser == null ||
                !"ARTIST".equalsIgnoreCase(loggedInUser.getRole())) {
            logger.error("Only artists can view play counts");
            return;
        }

        Artist artist = artistService.viewArtistProfile(loggedInUser.getUserId());
        if (artist == null) {
            logger.error("Create artist profile first");
            return;
        }

        List<Song> songs = songService.getSongsWithPlayCount(artist.getArtistId());
        if (songs.isEmpty()) {
            logger.error("No songs found");
            return;
        }

        logger.info("\n--- Play Count ---");
        for (Song s : songs) {
            logger.info("Song: {} | Plays: {}",
                    s.getTitle(), s.getPlayCount());
        }
    }

    // --- ARTIST SONG FAVORITED ---
    public void viewFavoriteAnalytics() {
        Integer artistId = getArtistId();
        if (artistId == null) return;
        var data = songService.getArtistFavoritesAnalytics(artistId);
        if (data.isEmpty()) {
            logger.info("No users have favorited your songs yet");
            return;
        }

        logger.info("\n--- Users Who Favorited Your Songs ---");
        data.forEach(logger::info);
    }



}
