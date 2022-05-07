package com.college.os_project.model.kernel;

import com.college.os_project.model.Bootloader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ProcessScheduler {
    public static ArrayList<Process> allProcesses = new ArrayList<>();
    public static Queue<Process> readyQueue = new LinkedList<>();
    public static int timeQuantum = 1;

    public ProcessScheduler() {

    }

    public static void loadProcesses() throws IOException {
        String fileName = "processes.txt";
        ClassLoader classLoader = Bootloader.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        String record;
        while ((record = reader.readLine()) != null) {
            String[] processInfo = record.split(" ");
            Process p = new Process(processInfo[0], Integer.parseInt(processInfo[1]), Integer.parseInt(processInfo[2]));
            readyQueue.add(p);
        }
    }

    public static void runProcesses() {
        while (readyQueue.size() > 0) {
            Process next = readyQueue.poll();
            runProcess(next);
        }

        System.out.println("There are no processes to be executed!");
    }

    public static void runProcess(Process process) {
        int PID = process.getPID();

        if (PID == 0) {
            System.out.println("Process is already in running state.");
        } else if (process.getProcessState().equals(ProcessState.READY)) {
            Bootloader.memoryManager.loadProcess(process);
            process.setState(ProcessState.RUNNING);
            process.setStartTime(System.currentTimeMillis());
            process.start();
            System.out.printf("Process with PID = %d is running.\n", PID);
        } else if (process.getProcessState().equals(ProcessState.BLOCKED)) {
            System.out.printf("Process with PID = %d is blocked, you need to unblock it first.\n", PID);
        } else if (process.getProcessState().equals(ProcessState.RUNNING)) {
            System.out.printf("Process with PID = %d is already in running state.\n", PID);
        } else if (process.getProcessState().equals(ProcessState.TERMINATED)) {
            System.out.printf("Process with PID = %d is terminated.\n", PID);
        }
    }

    public static Process getProcess(int PID) {
        for (Process p : allProcesses) {
            if (p.getPID() == PID) {
                return p;
            }
        }

        return null;
    }

    public static void showAllProcesses() {
        System.out.printf("%-3s\t\t %-18s\t\t %-3s\t\t %-10s\t\t %-10s\t\t %-12s\t\t %-12s\n", "PID", "USER", "PR", "STATE", "MEM", "START", "TIME+");
        for (Process p : allProcesses) {
            System.out.print(p);
        }
    }
}
