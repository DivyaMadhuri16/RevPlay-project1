package org.example.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // hash password before saving
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // verify login password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
