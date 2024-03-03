package com.example.csci3130_group_3;

import androidx.annotation.NonNull;

import java.util.Random;

public final class RandomStringGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    // Utility class cannot be instantiated.
    private RandomStringGenerator() {}

    /**
     * Generates a random string with the given length.
     * @param length The number of characters to generate in the string.
     * @return The random string of the given length.
     */
    public static @NonNull String generate(int length) {
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            char newChar = CHARACTERS.charAt(index);
            randomString.append(newChar);
        }

        return randomString.toString();
    }
}
