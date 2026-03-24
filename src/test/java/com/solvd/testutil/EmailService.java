package com.solvd.testutil;

import java.util.Random;

public class EmailService {

    public static String getRandomEmail() {
        return "updated" + (int) (Math.random() * 100) + "@mail.com";
    }

}
