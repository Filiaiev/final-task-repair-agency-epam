package com.filiaiev.agency.database.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

    private Hasher(){}

    private static final String algorithm = "SHA-256";

    /**
     * Using SHA-256 algorithm to convert String input
     * to it`s encoded value.
     * Used to check user login
     *
     * @see com.filiaiev.agency.web.command.auth.LogInCommand
     *
     * @param input String to encode
     *
     * @return encoded String value
     */
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
        return output.toString();
    }
}
