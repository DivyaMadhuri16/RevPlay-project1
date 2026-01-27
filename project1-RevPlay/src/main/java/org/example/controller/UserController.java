package org.example.controller;

import org.example.model.*;
import org.example.service.*;

import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private UserService userService = new UserService();
    private SearchService searchService = new SearchService();
    private SongService songService = new SongService();
    private FavoriteService favoriteService = new FavoriteService();
    private PlayerService playerService = new PlayerService();
    private ListeningHistoryService historyService = new ListeningHistoryService();
    private PlaylistService playlistService = new PlaylistService();

    private Scanner sc;
    private User loggedInUser;
    public User getLoggedInUser() {
        return loggedInUser;
    }

    private void displaySongs(List<Song> songs) {
        if (songs.isEmpty()) {
            logger.info("No songs found");
            return;
        }

        logger.info("\n--- Songs ---");
        for (Song s : songs) {
            logger.info("{} : {}", s.getSongId(), s.getTitle());
        }
    }

    public UserController(Scanner sc) {
        this.sc = sc;
    }

    // ---- REGISTER -----
    public void register() {
        logger.info("Enter Email: ");
        String email = sc.next();

        logger.info("Enter Password: ");
        String password = sc.next();

        logger.info("Enter Role (USER / ARTIST): ");
        String role = sc.next();

        logger.info("Choose Security Question: ");
        logger.info("1. What is your pet name? ");
        logger.info("2. What is your favorite color? ");
        logger.info("3. What is your birth city? ");
        logger.info("Choose Security Question: ");

        int qchoice;
        try {
            qchoice = Integer.parseInt(sc.next());
        } catch (NumberFormatException e) {
            logger.error("Invalid choice for security question");
            return;
        }
        String securityQuestion;
        switch (qchoice) {
            case 1 -> securityQuestion = "What is your pet name?";
            case 2 -> securityQuestion = "What is your favorite color?";
            case 3 -> securityQuestion = "What is your birth city?";
            default -> {
                logger.error("Invalid security question option");
                return;
            }
        }

        logger.info("Enter Security Answer: ");
        sc.nextLine();
        String securityAnswer = sc.nextLine();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setSecurityQuestion(securityQuestion);
        user.setSecurityAnswer(securityAnswer);

        boolean registered = userService.register(user);
        if (registered) {
            logger.info("Registration Successfull");
        } else {
            logger.info("user already exists or invalid input");
        }
    }

    // ------- LOGIN ------
    public User login() {
        logger.info("Enter Email: ");
        String email = sc.next();

        logger.info("Enter Password: ");
        String password = sc.next();

        User user = userService.login(email, password);
        if (user != null) {
            loggedInUser = user;
            logger.info("Login successful. Role: {}", user.getRole());
        } else {
            logger.error("Invalid credentials");
        }
        return user;
    }

    // --------CHANGE PASSWORD -------
    public void changePassword() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }

        logger.info("Enter Old Password:");
        String oldPassword = sc.next();

        logger.info("Enter New Password:");
        String newPassword = sc.next();

        boolean updated =
                userService.changePassword(loggedInUser.getEmail(), oldPassword, newPassword);
        if (updated) {
            logger.info("Password changed successfully");
        } else {
            logger.error("Old password is incorrect or new password is same as old");
        }
    }

    // -------- FORGOT PASSWORD -------
    public void forgotPassword() {
        logger.info("Enter Email:");
        String email = sc.next();

        String question = userService.getSecurityQuestion(email);
        if (question == null) {
            logger.error("Email not found");
            return;
        }

        logger.info("Security Question (Hint):");
        logger.info(question);

        logger.info("Enter Answer:");
        sc.nextLine();
        String answer = sc.nextLine();

        logger.info("Enter New Password:");
        String newPassword = sc.next();

        boolean success = userService.recoverPassword(email, answer, newPassword);
        if (success) {
            logger.info("Password reset successful");
        } else {
            logger.error("Security answer incorrect or new password same as old password");
        }
    }

    // --- SEARCH MENU ---
    public void search() {
        logger.info("Enter keyword to search:");
        sc.nextLine();
        String keyword = sc.nextLine();

        logger.info("\n--- Songs ---");
        var songs = searchService.searchSongs(keyword);
        if (songs.isEmpty()) {
            logger.info("No songs found");
        } else {
            for (Song s : songs) {
                logger.info("{} | Genre: {}", s.getTitle(), s.getGenre());
            }
        }

        logger.info("\n--- Artists ---");
        var artists = searchService.searchArtists(keyword);
        if (artists.isEmpty()) {
            logger.info("No artists found");
        } else {
            for (Artist a : artists) {
                logger.info("{} | Genre: {}", a.getArtistName(), a.getGenre());
            }
        }

        logger.info("\n--- Albums ---");
        var albums = searchService.searchAlbums(keyword);
        if (albums.isEmpty()) {
            logger.info("No albums found");
        } else {
            for (Album a : albums) {
                logger.info("{}", a.getAlbumTitle());
            }
        }
    }

    // --- BROWSE MENU ---
    public void browseSongs() {

        logger.info("\nBrowse Songs By:");
        logger.info("1. Genre");
        logger.info("2. Artist");
        logger.info("3. Album");
        logger.info("Enter choice:");

        int choice = Integer.parseInt(sc.next());
        switch (choice) {
            case 1 -> {
                logger.info("Enter Genre:");
                sc.nextLine();
                String genre = sc.nextLine();

                var songs = songService.browseByGenre(genre);
                displaySongs(songs);
            }
            case 2 -> {
                logger.info("Enter Artist ID:");
                int artistId = Integer.parseInt(sc.next());

                var songs = songService.browseByArtist(artistId);
                displaySongs(songs);
            }
            case 3 -> {
                logger.info("Enter Album ID:");
                int albumId = Integer.parseInt(sc.next());

                var songs = songService.browseByAlbum(albumId);
                displaySongs(songs);
            }
            default -> logger.error("Invalid option");
        }
    }

    // --- FAVORITES MENU ---
    public void favoritesMenu() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }

        logger.info("\n--- Favorites Menu ---");
        logger.info("1. Add Favorite");
        logger.info("2. Remove Favorite");
        logger.info("3. View Favorites");
        logger.info("4. Back");
        logger.info("Enter choice:");

        int choice = Integer.parseInt(sc.next());
        switch (choice) {
            case 1 -> {
                logger.info("Enter Song ID to add to favorites:");
                int songId = Integer.parseInt(sc.next());
                if (favoriteService.addFavorite(
                        loggedInUser.getUserId(), songId)) {
                    logger.info("Song added to favorites");
                } else {
                    logger.error("Failed to add favorite");
                }
            }
            case 2 -> {
                logger.info("Enter Song ID to remove from favorites:");
                int songId = Integer.parseInt(sc.next());
                if (favoriteService.removeFavorite(
                        loggedInUser.getUserId(), songId)) {
                    logger.info("Song removed from favorites");
                } else {
                    logger.error("Failed to remove favorite");
                }
            }
            case 3 -> {
                var favorites =
                        favoriteService.viewFavorites(
                                loggedInUser.getUserId());
                if (favorites.isEmpty()) {
                    logger.info("No favorite songs found");
                } else {
                    logger.info("--- Your Favorite Songs ---");
                    for (Song s : favorites) {
                        logger.info("{} : {}", s.getSongId(), s.getTitle());
                    }
                }
            }
            case 4 -> {
                return;
            }
            default -> logger.error("Invalid option");
        }
    }

    // --- PLAYER MENU ---
    public void playerMenu() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }

        logger.info("Enter Genre to load playlist:");
        sc.nextLine();
        String genre = sc.nextLine();

        var songs = songService.browseByGenre(genre);
        if (songs.isEmpty()) {
            logger.error("No songs found");
            return;
        }

        playerService.loadQueue(songs);
        boolean running = true;
        while (running) {

            logger.info("\n--- Player ---");
            logger.info("1. Play");
            logger.info("2. Pause");
            logger.info("3. Next");
            logger.info("4. Repeat (toggle)");
            logger.info("5. View History");
            logger.info("6. Exit Player");

            int choice = Integer.parseInt(sc.next());
            switch (choice) {
                case 1 -> {
                    Song s = playerService.play(loggedInUser.getUserId());
                    logger.info("Playing: {}", s.getTitle());
                }
                case 2 -> {
                    Song s = playerService.pause();
                    logger.info("Paused: {}", s.getTitle());
                }
                case 3 -> {
                    Song s = playerService.next(loggedInUser.getUserId());
                    logger.info("Now Playing: {}", s.getTitle());
                }
                case 4 -> {
                    playerService.toggleRepeat();
                    logger.info("Repeat mode toggled");
                }
                case 5 -> {
                    var history =
                            playerService.getHistory(loggedInUser.getUserId());

                    if (history.isEmpty()) {
                        logger.info("No listening history");
                    } else {
                        logger.info("--- History ---");
                        history.forEach(h ->
                                logger.info("Song ID: {} at {}",
                                        h.getSongId(), h.getPlayedAt()));
                    }
                }
                case 6 -> running = false;
                default -> logger.error("Invalid option");
            }
        }
    }

    // --- LISTENING HISTORY ---
    public void viewListeningHistory() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }

        var history = historyService.viewHistory(loggedInUser.getUserId());
        if (history.isEmpty()) {
            logger.info("No listening history found");
            return;
        }

        logger.info("\n--- Listening History ---");
        history.forEach(logger::info);
    }

    // --- RECENTLY PLAYED ---
    public void viewRecentlyPlayedSongs() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }

        var recent = historyService.viewRecentlyPlayed(loggedInUser.getUserId());
        if (recent.isEmpty()) {
            logger.info("No recently played songs");
            return;
        }

        logger.info("\n--- Recently Played Songs ---");
        recent.forEach(logger::info);
    }

    // --- CREATE PLAYLIST ---
    public void createPlaylist() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }

        logger.info("Enter Playlist Name:");
        sc.nextLine();
        String name = sc.nextLine();

        logger.info("Enter Description:");
        String description = sc.nextLine();

        logger.info("Privacy (PUBLIC / PRIVATE):");
        String privacy = sc.next().toUpperCase();

        Playlist playlist = new Playlist();
        playlist.setUserId(loggedInUser.getUserId());
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setPrivacy(privacy);

        boolean created = playlistService.createPlaylist(playlist);
        if (created) {
            logger.info("Playlist created successfully");
        } else {
            logger.error("Failed to create playlist");
        }

    }

    // --- ADD SONG TO PLAYLIST ---
    public void addSongToPlaylist() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }

        logger.info("Enter Playlist ID:");
        int playlistId = sc.nextInt();

        var songs = songService.getAllSongs();
        if (songs.isEmpty()) {
            logger.info("No songs available");
            return;
        }

        logger.info("\n--- Available Songs ---");
        for (Song s : songs) {
            logger.info(s.getSongId() + " : " + s.getTitle());
        }

        logger.info("Enter Song ID to add:");
        int songId = sc.nextInt();

        boolean added = playlistService.addSongToPlaylist(loggedInUser.getUserId(), playlistId, songId);
        if (added) {
            logger.info("Song added to playlist successfully");
        } else {
            logger.error("Failed to add song");
        }
    }

    // --- REMOVE SONG FROM PLAYLIST ---
    public void removeSongFromPlaylist() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }

        logger.info("Enter Playlist ID:");
        int playlistId = sc.nextInt();

        var songs = playlistService.getSongsInPlaylist(playlistId);
        if (songs.isEmpty()) {
            logger.info("No songs in this playlist");
            return;
        }

        logger.info("\n--- Songs in Playlist ---");
        songs.forEach(logger::info);

        logger.info("Enter Song ID to remove:");
        int songId = sc.nextInt();

        boolean removed = playlistService.removeSongFromPlaylist(loggedInUser.getUserId(), playlistId, songId);
        if (removed) {
            logger.info("Song removed from playlist successfully");
        } else {
            logger.error("Failed to remove song");
        }
    }

    // --- VIEW PLAYLIST ---
    public void viewMyPlaylists() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }

        var playlists = playlistService.getUserPlaylists(loggedInUser.getUserId());
        if (playlists.isEmpty()) {
            logger.info("You have no playlists");
            return;
        }

        logger.info("\n--- My Playlists ---");
        for (Playlist p : playlists) {
            logger.info(
                    p.getPlaylistId() + " | " +
                            p.getName() + " | " +
                            p.getPrivacy() + " | " +
                            p.getDescription()
            );
        }
    }

    // --- UPDATE PLAYLIST ---
    public void updatePlaylist() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }

        viewMyPlaylists();

        logger.info("Enter Playlist ID to update:");
        int playlistId = sc.nextInt();
        sc.nextLine();

        logger.info("Enter New Playlist Name:");
        String name = sc.nextLine();

        logger.info("Enter New Description:");
        String description = sc.nextLine();

        logger.info("Privacy (PUBLIC / PRIVATE):");
        String privacy = sc.next().toUpperCase();

        Playlist playlist = new Playlist();
        playlist.setPlaylistId(playlistId);
        playlist.setUserId(loggedInUser.getUserId());
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setPrivacy(privacy);

        boolean updated = playlistService.updatePlaylist(playlist);
        if (updated) {
            logger.info("Playlist updated successfully");
        } else {
            logger.error("Failed to update playlist");
        }
    }

    // --- DELETE PLAYLIST ---
    public void deletePlaylist() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }
        // show playlists first
        viewMyPlaylists();

        logger.info("Enter Playlist ID to delete:");
        int playlistId = sc.nextInt();

        logger.info("Are you sure? (yes/no):");
        String confirm = sc.next();

        if (!confirm.equalsIgnoreCase("yes")) {
            logger.info("Playlist deletion cancelled");
            return;
        }

        boolean deleted = playlistService.deletePlaylist(playlistId, loggedInUser.getUserId());
        if (deleted) {
            logger.info("Playlist deleted successfully");
        } else {
            logger.error("Failed to delete playlist");
        }
    }

    // --- PUBLIC PLAYLIST ---
    public void viewPublicPlaylists() {
        if (loggedInUser == null) {
            logger.error("Please login first");
            return;
        }

        var playlists = playlistService.viewPublicPlaylists(loggedInUser.getUserId());
        if (playlists.isEmpty()) {
            logger.info("No public playlists available");
            return;
        }

        logger.info("\n--- Public Playlists ---");
        playlists.forEach(logger::info);
    }


}
