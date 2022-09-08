package com.college.os_project.model.processor;

import com.college.os_project.model.Bootloader;
import com.college.os_project.model.memory.MemoryManager;

import java.util.ArrayList;
import java.util.PriorityQueue;


public class ProcessScheduler extends Thread {
    private final static CPU cpu = Bootloader.getCpu();
    private final static MemoryManager memoryManager = Bootloader.getMemoryManager();
    private static Process activeProcess;
    public static ArrayList<Process> allProcesses = new ArrayList<>();

    public ProcessScheduler() {
    }

    public static PriorityQueue<Process> readyQueue = new PriorityQueue<>((o1, o2) -> {
        if (o1.getProcessPriority() < o2.getProcessPriority()) {
            return -1;
        } else if (o1.getProcessPriority() > o2.getProcessPriority()) {
            return 1;
        } else {
            return 0;
        }
    });

    public void run() {
        // dev
        while (!readyQueue.isEmpty()) {
            Process next = readyQueue.poll();
            runProcess(next);

            if (!next.isBlocked() && !next.isTerminated() && !next.isSuspended() && !next.isDone()) {
                readyQueue.add(next);
            }
        }
    }

    private static void runProcess(Process process) {
        cpu.setActiveProcess(process);

        if (process.getPC() == -1) {
            System.out.printf("Process with PID = %d started execution.\n", process.getPID());
            memoryManager.loadProcess(process);
            CPU.setPCvalue(0);
            process.setState(ProcessState.RUNNING);
            cpu.execute(process, System.currentTimeMillis());
        } else {
            System.out.printf("Process with PID = %d continued with execution.\n", process.getPID());
            cpu.loadRegistersValues();
            process.setState(ProcessState.RUNNING);
            cpu.execute(process, System.currentTimeMillis());
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

    public static Process getNextProcess() {
        if ((CPU.getActiveProcess() == null || CPU.getActiveProcess().isTerminated())) {
            activeProcess = readyQueue.peek();
        }
        return activeProcess;
    }

    public static void stopProcess(Integer PID) {
        if (PID < allProcesses.size()) {
            allProcesses.get(PID).terminateProcess();
            return;
        }
        System.out.printf("Process with PID = %d does not exist.\n", PID);
    }

    public static void blockProcess(Integer PID) {
        if (PID < allProcesses.size()) {
            allProcesses.get(PID).blockProcess();
            return;
        }
        System.out.printf("Process with PID = %d does not exist.\n", PID);
    }

    public static void unblockProcess(Integer PID) {
        if (PID < allProcesses.size()) {
            allProcesses.get(PID).unblockProcess();
            return;
        }
        System.out.printf("Process with PID = %d does not exist.\n", PID);
    }

    public static void showAllProcesses() {
        System.out.printf("%-3s\t\t %-18s\t\t %-3s\t\t %-10s\t\t %-10s\t\t %-12s\t\t %-12s\n", "PID", "NAME", "PR", "STATE", "MEM", "START", "TIME+");
        for (Process p : allProcesses) {
            System.out.print(p);
        }
    }
}
