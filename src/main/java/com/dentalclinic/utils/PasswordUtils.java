package com.dentalclinic.utils;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtils {

    private PasswordUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
