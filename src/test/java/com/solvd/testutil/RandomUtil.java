package com.solvd.testutil;

import java.util.Random;

public class RandomUtil {

    private static final Random RANDOM = new Random();

    public static String randomEmail() {
        return "user" + RANDOM.nextInt(100000) + "@randoMmail.com";
    }

    public static String randomName() {
        return "user" + RANDOM.nextInt(100000);
    }
}