package com.college.os_project.model.processor;

import com.college.os_project.model.Bootloader;
import com.college.os_project.model.memory.Memory;
import com.college.os_project.model.memory.MemoryManager;
import com.college.os_project.model.memory.MemoryPartition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Process {
    private int PID;
    private String username;
    private String name;
    private ProcessState state;
    private int priority;
    private int size;
    private long startTime;
    private MemoryPartition partition;
    private long totalTimeMS = 0;
    private ArrayList<String> instructions = new ArrayList<>();
    private int[] valuesOfRegisters;
    private int PC = -1;
    private Path filePath;
    private int instructionsSize;

    public Process(String name, String fullFileName, int priority) throws IOException {
        if (Bootloader.getMemory().contains(fullFileName)) {
            this.filePath = Paths.get(Bootloader.getTree().getCurrentFolder().getAbsolutePath() + "/" + fullFileName);
            this.name = name;
            this.PID = ProcessScheduler.allProcesses.size();
            this.state = ProcessState.READY;
            this.priority = priority;
            this.valuesOfRegisters = new int[4];
            readFile();
            this.instructionsSize = instructions.size();
            for (int i = 0; i < instructionsSize; i++) {
                this.size += instructions.get(i).length();
            }
            this.size = Math.round(this.size / 8) + 16;
            ProcessScheduler.allProcesses.add(this);
            ProcessScheduler.readyQueue.add(this);
        } else {
            System.out.println("File does not exist.");
        }
    }

    public Process(String username, int priority, int size) {
        this.PID = ProcessScheduler.allProcesses.size();
        this.username = username;
        this.state = ProcessState.READY;
        this.priority = priority;
        this.size = size;
        this.totalTimeMS = this.size;

        ProcessScheduler.allProcesses.add(this);
    }

    public Process(String username, int priority, ProcessState ps, int size) {
        this.PID = ProcessScheduler.allProcesses.size();
        this.username = username;
        this.state = ps;
        this.priority = priority;
        this.size = size;

        ProcessScheduler.allProcesses.add(this);
    }

    private void readFile() throws IOException {
        List<String> content = Files.readAllLines(filePath);

        for (String instruction : content) {
            String machineCode = Bootloader.getAssembler().transformToMachineCode(instruction);
            this.instructions.add(machineCode);
        }
    }

    public void blockProcess() {
        if (this.isRunning()) {
            this.state = ProcessState.BLOCKED;
            System.out.printf("Process with PID = %d is blocked.\n", PID);

            if (ProcessScheduler.readyQueue.contains(this)) {
                ProcessScheduler.readyQueue.remove(this);
            }
        } else {
            System.out.printf("Process with PID = %d is not in running state.\n", PID);
        }
    }

    public void unblockProcess() {
        if (this.isBlocked()) {
            this.state = ProcessState.READY;
            System.out.printf("Process with PID = %d is unblocked.\n", PID);
            ProcessScheduler.readyQueue.add(this);
        } else {
            System.out.printf("Process with PID = %d is not blocked.\n", PID);
        }
    }

    public void terminateProcess() {
        if (this.isReady() || this.isRunning()) {
            this.state = ProcessState.TERMINATED;
            MemoryManager.removeProcess(this);
            System.out.printf("Process with PID = %d is terminated.\n", this.getPID());

            if (ProcessScheduler.readyQueue.contains(this)) {
                ProcessScheduler.readyQueue.remove(this);
            }
        } else if (this.isBlocked()) {
            this.state = ProcessState.TERMINATED;
            MemoryManager.removeProcess(this);
            System.out.printf("Process with PID = %d is terminated.\n", this.getPID());
        }
    }

    public int getPID() {
        return this.PID;
    }

    public ProcessState getProcessState() {
        return this.state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public boolean isRunning() {
        return this.state.equals(ProcessState.RUNNING);
    }

    public boolean isBlocked() {
        return this.state.equals(ProcessState.BLOCKED);
    }

    public boolean isReady() {
        return this.state.equals(ProcessState.READY);
    }

    public boolean isTerminated() {
        return this.state.equals(ProcessState.TERMINATED);
    }

    public boolean isSuspended() {
        return this.state.equals(ProcessState.SUSPENDED);
    }

    public boolean isDone() {
        return this.state.equals(ProcessState.DONE);
    }

    public int getProcessPriority() {
        return this.priority;
    }

    public int getSize() {
        return this.size;
    }

    public String getInstruction(int index) {
        return instructions.get(index);
    }

    public int getInstructionsSize() {
        return instructionsSize;
    }

    public int getPC() {
        return PC;
    }

    public int[] getRegisters() {
        return valuesOfRegisters;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setValuesOfRegisters(int[] registersValues) {
        for (int i = 0; i < registersValues.length; i++) {
            this.valuesOfRegisters[i] = registersValues[i];
        }
    }

    public boolean loadProcess(MemoryPartition partition) {
        this.partition = Memory.occupyPartition(partition, this);
        if (partition == null) {
            return false;
        } else {
            return true;
        }
    }

    public long getTotalTimeMS() {
        return totalTimeMS;
    }

    public void setEndTime(long currentTimeMillis) {
        this.totalTimeMS = currentTimeMillis - startTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

        if (startTime == 0) {
            sb.append(String.format("%-3s\t\t %-18s\t\t %-3s\t\t %-10s\t\t %-10s\t\t %-12s\t\t %-12s\n", this.PID, this.name, this.priority, this.state, this.size, "?", "?"));
        } else {
            Duration duration = Duration.ofMillis(totalTimeMS);
            long seconds = duration.getSeconds();
            long HH = seconds / 3600;
            long mm = (seconds % 3600) / 60;
            long ss = seconds % 60;
            String totalTime = String.format("%02d:%02d:%02d", HH, mm, ss);

            sb.append(String.format("%-3s\t\t %-18s\t\t %-3s\t\t %-10s\t\t %-10s\t\t %-12s\t\t %-12s\n", this.PID, this.name, this.priority, this.state, this.size, df.format(this.startTime), totalTime));
        }

        return sb.toString();
    }
}
