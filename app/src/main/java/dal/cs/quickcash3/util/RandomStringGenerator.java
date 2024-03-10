package dal.cs.quickcash3.util;

import androidx.annotation.NonNull;

import java.util.Random;

public final class RandomStringGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    // Utility class constructor.
    private RandomStringGenerator() {}

    /**
     * Generate a random string of characters with the given length.
     * @param length The length of characters to generate.
     * @return The string of random characters.
     */
    public static @NonNull String generate(int length) {
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            char nextCharacter = CHARACTERS.charAt(index);
            randomString.append(nextCharacter);
        }

        return randomString.toString();
    }
}
