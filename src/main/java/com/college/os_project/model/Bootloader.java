package com.college.os_project.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Bootloader {
    public static boolean boot() {
        try {
            displayInfo();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    private static void displayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("OS simulator: ").append("\n");
        sb.append("Memory: ").append("\n");
        System.out.println(sb);
    }

    public static boolean login(String username, String password) throws IOException {
        String fileName = "user_credentials.txt";
        ClassLoader classLoader = Bootloader.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        String record;
        while ((record = reader.readLine()) != null) {
            String[] userCredentials = record.split(" ");

            if (userCredentials[0].equals(username) && userCredentials[1].equals(password)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
}
