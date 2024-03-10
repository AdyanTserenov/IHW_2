package org.example.tools;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordCipher {
    public static byte[] getCipher(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ignored) {}

        return "".getBytes();
    }

    public static String getHexString(byte[] cipher) {
        BigInteger number = new BigInteger(1, cipher);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
}
