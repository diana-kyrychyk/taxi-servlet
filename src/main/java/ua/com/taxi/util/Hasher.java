package ua.com.taxi.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

    private Hasher() {
    }

    public static String hash(String originString) {
        String hashResult = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(originString.getBytes());
            byte[] bytes = md.digest();
            hashResult = convertToHexadecimal(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashResult;
    }

    private static String convertToHexadecimal(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
