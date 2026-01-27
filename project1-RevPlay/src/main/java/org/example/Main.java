package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.ArtistController;
import org.example.controller.UserController;
import org.example.model.User;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static Scanner sc = new Scanner(System.in);
    private static UserController userController = new UserController(sc);
    private User loggedInUser;
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public static void main(String[] args){
        while (true){
            logger.info("\n --- RevPlay Menu --- ");
            logger.info("1. Register");
            logger.info("2. Login");
            logger.info("3. Forgot password");
            logger.info("4. Change Password");
            logger.info("5. Artist Menu");
            logger.info("6. Search");
            logger.info("7. Browse Songs");
            logger.info("8. Favorites");
            logger.info("9. Music Player");
            logger.info("10. View Listening History");
            logger.info("11. View Recently Played Songs");
            logger.info("12. Create Playlist");
            logger.info("13. Add Song to Playlist");
            logger.info("14. Remove Song from Playlist");
            logger.info("15. View My Playlists");
            logger.info("16. Update Playlist");
            logger.info("17. Delete Playlist");
            logger.info("18. View Public Playlists");
            logger.info("19. Exit");
            logger.info("Enter your choice");

            int choice;

            try {
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                logger.error("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice){
                case 1 -> userController.register();
                case 2 -> userController.login();
                case 3 -> userController.forgotPassword();
                case 4 -> userController.changePassword();
                case 5 -> openArtistMenu();
                case 6 -> userController.search();
                case 7 -> userController.browseSongs();
                case 8 -> userController.favoritesMenu();
                case 9 -> userController.playerMenu();
                case 10 -> userController.viewListeningHistory();
                case 11 -> userController.viewRecentlyPlayedSongs();
                case 12 -> userController.createPlaylist();
                case 13 -> userController.addSongToPlaylist();
                case 14 -> userController.removeSongFromPlaylist();
                case 15 -> userController.viewMyPlaylists();
                case 16 -> userController.updatePlaylist();
                case 17 -> userController.deletePlaylist();
                case 18 -> userController.viewPublicPlaylists();
                case 19 -> {
                    System.exit(0);
                    sc.close();
                }

                default -> logger.info("Invalid Choice");
            }
        }
    }

    private static void openArtistMenu() {
        User loggedUser = userController.getLoggedInUser();
        if (loggedUser == null) {
            logger.error("Please login first");
            return;
        }
        if (!"ARTIST".equalsIgnoreCase(loggedUser.getRole())) {
            logger.error("Only artists can access this menu");
            return;
        }

        ArtistController artistController = new ArtistController(sc, loggedUser);
        boolean artistMenu = true;

        while (artistMenu) {
            logger.info("\n--- Artist Menu ---");
            logger.info("1. Create Artist Profile");
            logger.info("2. View Artist Profile");
            logger.info("3. Update Artist Profile");
            logger.info("4. Upload Song");
            logger.info("5. Create Album");
            logger.info("6. Add Song to Album");
            logger.info("7. Update Song");
            logger.info("8. Update Album");
            logger.info("9. Delete Song");
            logger.info("10. View Play Count");
            logger.info("11. View Favorite Analytics");
            logger.info("12. Back");
            logger.info("Enter choice:");

            int artistchoice;
            try {
                artistchoice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                logger.error("Invalid input. Please enter a number.");
                sc.close();
                return;
            }

            switch (artistchoice) {
                case 1 -> artistController.createArtistProfile();
                case 2 -> artistController.viewArtistProfile();
                case 3 -> artistController.updateArtistProfile();
                case 4 -> artistController.uploadSong();
                case 5 -> artistController.createAlbum();
                case 6 -> artistController.addSongToAlbum();
                case 7 -> artistController.updateSong();
                case 8 -> artistController.updateAlbum();
                case 9 -> artistController.deleteSong();
                case 10 -> artistController.viewPlayCount();
                case 11 -> artistController.viewFavoriteAnalytics();
                case 12 -> artistMenu = false;
                default -> logger.error("Invalid option");
            }
        }
    }
}