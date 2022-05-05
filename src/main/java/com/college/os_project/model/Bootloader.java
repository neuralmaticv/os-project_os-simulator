package com.college.os_project.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

    public static boolean login(String username, String password) {
        List<String> credentials = null;

        try {
            credentials = Files.readAllLines(Path.of("/home/vladimir/GoogleDrive/Courses/03Third_semester/projects/os-project/src/main/java/com/college/os_project/model/user_credentials.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String record : credentials) {
            String[] userCredentials = record.split(" ");

            if (userCredentials[0].equals(username) && userCredentials[1].equals(password)) {
                return true;
            } else {
                return false;
            }
        }

        return true;
    }
}
