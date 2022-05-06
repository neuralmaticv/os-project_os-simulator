package com.college.os_project.view;

import com.college.os_project.model.Bootloader;
import com.college.os_project.model.Commands;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class ConsoleView implements View {
    @Override
    public void drawView() {
        if (Bootloader.boot()) {
            System.out.println("Successfully booted.");
        } else {
            System.out.println("Boot error occured.");
            return;
        }

        String hostname = "UNKNOWN HOST NAME";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Operating System Simulator").append("[Version 0.0.1]").append("\n");
        sb.append("Authors: ").append("Vladimir Mijic").append("\n");
        sb.append("On computer: ").append(hostname).append("\n");
        sb.append("-------------------------------------------------------------------\n\n");

        System.out.println(sb);
        try {
            login();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void login() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("[?] username: ");
        String username = scanner.next();

        System.out.print("[?] password: ");
        String password = scanner.next();

        if (Bootloader.login(username, getHashValue(password))) {
            System.out.println("Successfully logged in. Welcome, " + username);
            scanner.nextLine();

            while (true) {
                System.out.print(username + ":~$ ");
                String userInput = scanner.nextLine();
                Commands.checkCommand(userInput);
            }

        } else {
            System.out.println("Incorrect login details. Sorry, try again.");
            login();
        }
    }


    private static String getHashValue(String password) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] bytesOfPassword = password.getBytes(StandardCharsets.UTF_8);
            byte[] hash = md5.digest(bytesOfPassword);

            // Convert hash into HEX value
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
