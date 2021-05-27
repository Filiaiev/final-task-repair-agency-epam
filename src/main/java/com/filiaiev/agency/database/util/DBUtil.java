package com.filiaiev.agency.database.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DBUtil {

    private DBUtil(){}

    private static String algorithm = "SHA-256";

    public static String hash(String input) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        digest.update(input.getBytes());
        byte[] hash = digest.digest();
        StringBuilder output = new StringBuilder();

        for(byte b : hash){
            output.append(Integer.toHexString((b >> 4)&0xF)).append(Integer.toHexString(b&0xF));
        }
        return output.toString().toUpperCase();
    }
}
