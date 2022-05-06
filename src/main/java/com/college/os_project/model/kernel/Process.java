package com.college.os_project.model.kernel;

import com.college.os_project.model.Bootloader;
import com.college.os_project.model.memory.MemoryManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

public class Process extends Thread {
    private final int PID;
    private String username;
    private ProcessState state;
    private int priority;
    private int size;
    private Date startTime;
    private int totalTimeMS = 0;
    private int programCounter = -1;
    private ArrayList<String> instructions;
    private int startAddress;
    ; // Address of program instructions in main memory
    private int[] valuesOfRegisters; // To remember values of registers when switching to next process
    private static ArrayList<Process> listOfProcesses = new ArrayList<>();

    public Process(String username, int priority, int size) {
        this.PID = listOfProcesses.size();
        this.username = username;
        this.state = ProcessState.READY;
        this.priority = priority;
        this.size = size;
        this.setDaemon(false);

        listOfProcesses.add(this);
        // TODO: add process to ProcessScheduler after creating it
    }

    public Process(String username, int priority, ProcessState ps, int size, boolean isDaemon) {
        this.PID = listOfProcesses.size();
        this.username = username;
        this.state = ps;
        this.priority = priority;
        this.size = size;

        if (isDaemon) {
            this.setDaemon(true);
            this.startTime = new Date();
        } else {
            this.setDaemon(false);
        }

        listOfProcesses.add(this);
        // TODO: add process to ProcessScheduler after creating it
    }

    public Process(String program) {
        // TODO:
        this.PID = listOfProcesses.size();
        this.username = "";
        this.state = ProcessState.READY;
        this.priority = 100;
        this.instructions = new ArrayList<>();
        this.valuesOfRegisters = new int[4];
        readProgram();
        this.size = instructions.size();
        listOfProcesses.add(this);

        // TODO: add process to ProcessScheduler after creating it
    }

    @Override
    public void run() {
        while (!this.getProcessState().equals(ProcessState.TERMINATED)) {
            if (this.getPID() != 0) {
                Process.getProcess(0).incSize(0);
            }

            try {
                if (this.getProcessState().equals(ProcessState.RUNNING)) {
                    Thread.sleep(10);
                    totalTimeMS += 10;
                } else {
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Process getProcess(int PID) {
        for (Process p : listOfProcesses) {
            if (p.getPID() == PID) {
                return p;
            }
        }

        return null;
    }

    public void runProcess() {
        if (PID == 0) {
            System.out.println("Process is already in running state.");
        } else if (this.state.equals(ProcessState.READY)) {
            this.state = ProcessState.RUNNING;
            this.startTime = new Date();
            this.start();
            System.out.printf("Process with PID = %d is running.\n", PID);
        } else if (this.state.equals(ProcessState.BLOCKED)) {
            System.out.printf("Process with PID = %d is blocked, you need to unblock it first.\n", PID);
        } else if (this.state.equals(ProcessState.RUNNING)) {
            System.out.printf("Process with PID = %d is already in running state.\n", PID);
        } else if (this.state.equals(ProcessState.TERMINATED)) {
            System.out.printf("Process with PID = %d is terminated.\n", PID);
        }
    }

    public void blockProcess() {
        if (PID == 0) {
            System.out.println("You are not allowed to block this process.");
        } else if (this.state.equals(ProcessState.RUNNING)) {
            this.state = ProcessState.BLOCKED;
            System.out.printf("Process with PID = %d is blocked.\n", PID);
        } else {
            System.out.printf("Process with PID = %d is not in running state.\n", PID);
        }
    }

    public void unblockProcess() {
        if (this.state.equals(ProcessState.BLOCKED)) {
            this.state = ProcessState.RUNNING;
            System.out.printf("Process with PID = %d is unblocked.\n", PID);
        } else {
            System.out.printf("Process with PID = %d is not blocked.\n", PID);
        }
    }

    public void terminateProcess() {
        if (PID == 0) {
            System.out.println("You are not allowed to terminate this process.");
        } else if (this.state.equals(ProcessState.READY) || this.state.equals(ProcessState.RUNNING)) {
            this.state = ProcessState.TERMINATED;
            System.out.printf("Process with PID = %d is terminated.\n", this.getPID());
        } else if (this.state.equals(ProcessState.BLOCKED)) {
            MemoryManager.removeProcess(this);
            this.state = ProcessState.TERMINATED;
            System.out.printf("Process with PID = %d is terminated.\n", this.getPID());
        }
    }

    public int getPID() {
        return this.PID;
    }

    public String getUsername() {
        return this.username;
    }

    public ProcessState getProcessState() {
        return this.state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public int getProcessPriority() {
        return this.priority;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void incSize(int size) {
        this.size += size;
    }

    public int getProgramCounter() {
        return this.programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public int getStartAddress() {
        return this.startAddress;
    }

    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }

    public int[] getValuesOfRegisters() {
        return this.valuesOfRegisters;
    }

    public void setValuesOfRegisters(int[] valuesOfRegisters) {
        this.valuesOfRegisters = valuesOfRegisters;
    }

    public ArrayList<String> getInstructions() {
        return this.instructions;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    private void readProgram() {
        // TODO:
        // read asm file and write machine instructions...
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
            new Process(processInfo[0], Integer.parseInt(processInfo[1]), Integer.parseInt(processInfo[2]));
        }
    }

    public static void showAllProcesses() {
        System.out.printf("%-3s\t\t %-18s\t\t %-3s\t\t %-10s\t\t %-10s\t\t %-12s\t\t %-12s\n", "PID", "USER", "PR", "STATE", "MEM", "START", "TIME+");
        for (Process p : listOfProcesses) {
            System.out.print(p);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        if (startTime == null) {
            sb.append(String.format("%-3s\t\t %-18s\t\t %-3s\t\t %-10s\t\t %-10s\t\t %-12s\t\t %-12s\n", this.PID, this.username, this.priority, this.state, this.size, "?", "?"));
        } else {
            Duration duration = Duration.ofMillis(totalTimeMS);
            long seconds = duration.getSeconds();
            long HH = seconds / 3600;
            long mm = (seconds % 3600) / 60;
            long ss = seconds % 60;
            String totalTime = String.format("%02d:%02d:%02d", HH, mm, ss);

            sb.append(String.format("%-3s\t\t %-18s\t\t %-3s\t\t %-10s\t\t %-10s\t\t %-12s\t\t %-12s\n", this.PID, this.username, this.priority, this.state, this.size, df.format(this.startTime), totalTime));
        }

        return sb.toString();
    }
}
