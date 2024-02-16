package com.example.csci3130_group_3;

import java.util.Random;

public class RandomStringGenerator {

    // A constant string that contains all the possible characters
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // A random number generator
    private static final Random RANDOM = new Random();

    // A method that takes an integer as the length of the desired string
    // and returns a random string of that length
    public static String generate(int length) {
        // A string builder to store the generated string
        StringBuilder sb = new StringBuilder();

        // A loop that iterates for the given length
        for (int i = 0; i < length; i++) {
            // A random index between 0 and the length of the characters string
            int index = RANDOM.nextInt(CHARACTERS.length());

            // A random character from the characters string
            char c = CHARACTERS.charAt(index);

            // Append the character to the string builder
            sb.append(c);
        }

        // Return the string builder as a string
        return sb.toString();
    }
}
