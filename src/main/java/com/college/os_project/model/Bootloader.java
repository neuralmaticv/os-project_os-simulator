package com.college.os_project.model;

import com.college.os_project.model.kernel.Process;
import com.college.os_project.model.kernel.ProcessScheduler;
import com.college.os_project.model.kernel.ProcessState;
import com.college.os_project.model.memory.MemoryManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Bootloader {
    public static MemoryManager memoryManager;

    public static void boot() throws IOException {
        memoryManager = new MemoryManager(4096);
        new ProcessScheduler();

        // init - systemd system and service manager
        Process systemd = new Process("systemd", -1, ProcessState.RUNNING, 350, true);
        systemd.start();
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
            }
        }

        return false;
    }
}
