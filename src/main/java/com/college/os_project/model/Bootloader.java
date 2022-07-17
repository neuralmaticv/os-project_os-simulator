package com.college.os_project.model;

import com.college.os_project.model.assembler.Assembler;
import com.college.os_project.model.memory.MemoryManager;
import com.college.os_project.model.processor.CPU;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Bootloader {
    private static CPU cpu;
    private static MemoryManager memoryManager;
    private static Assembler assembler;

    public static void boot() throws IOException {
        memoryManager = new MemoryManager(2048);
        assembler = new Assembler();
        cpu = new CPU(assembler);
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

    public static CPU getCpu() {
        return cpu;
    }

    public static MemoryManager getMemoryManager() {
        return memoryManager;
    }

    public static Assembler getAssembler() {
        return assembler;
    }
}
