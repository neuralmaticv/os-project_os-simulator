package com.college.os_project.model.processor;

import com.college.os_project.model.Bootloader;
import com.college.os_project.model.memory.Memory;
import com.college.os_project.model.memory.MemoryManager;
import com.college.os_project.model.memory.MemoryPartition;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Process {
    private int PID;
    private String name, fullFileName;
    private ProcessState state;
    private int priority;
    private int size;
    private long startTime;
    private long totalTimeMS = 0;
    private final ArrayList<String> instructions = new ArrayList<>();
    private int[] valuesOfRegisters;
    private int PC = -1;
    private int instructionsSize;
    private Path filePath;
    private int processOutput;
    private String outputFileName;

    public Process(String name, String fullFileName, String outputFileName, int priority) throws IOException {
        if (Bootloader.getMemory().contains(fullFileName)) {
            this.name = name;
            this.fullFileName = fullFileName;
            this.outputFileName = outputFileName;
            this.filePath = Paths.get(Bootloader.getTree().getCurrentFolder().getAbsolutePath() + "/" + fullFileName);
            this.priority = priority;
            this.state = ProcessState.READY;
            this.PID = ProcessScheduler.getAllProcesses().size();
            this.valuesOfRegisters = new int[4];
            readFile();
            this.instructionsSize = instructions.size();

            for (int i = 0; i < instructionsSize; i++) {
                this.size += instructions.get(i).length();
            }

            this.size = Math.round(this.size / 8) + 16;
            ProcessScheduler.getAllProcesses().add(this);
            ProcessScheduler.getReadyQueue().add(this);
        } else {
            System.out.println("File does not exist.");
        }
    }

    private void readFile() {
        List<String> content = List.of(Bootloader.getMemory().readFile(Bootloader.getMemory().getFile(this.fullFileName)).split("\n"));

        for (String instruction : content) {
            String machineCode = Bootloader.getAssembler().transformToMachineCode(instruction);
            this.instructions.add(machineCode);
        }
    }

    public void blockProcess() {
        if (this.isRunning()) {
            this.state = ProcessState.BLOCKED;
            System.out.printf("Process with PID = %d is blocked.\n", PID);

            ProcessScheduler.readyQueue.remove(this);
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
            this.outputFileName = null;
            MemoryManager.removeProcess(this);
            System.out.printf("Process with PID = %d is terminated.\n", this.getPID());

            ProcessScheduler.readyQueue.remove(this);
        } else if (this.isBlocked()) {
            this.state = ProcessState.TERMINATED;
            MemoryManager.removeProcess(this);
            System.out.printf("Process with PID = %d is terminated.\n", this.getPID());
        }
    }

    public int getPID() {
        return this.PID;
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
        System.arraycopy(registersValues, 0, this.valuesOfRegisters, 0, registersValues.length);
    }

    public boolean loadProcess(MemoryPartition memoryPartition) {
        MemoryPartition partition = Memory.occupyPartition(memoryPartition, this);

        return partition != null;
    }

    public void setEndTime(long currentTimeMillis) {
        this.totalTimeMS = currentTimeMillis - startTime;
    }

    public int getProcessOutput() {
        return processOutput;
    }

    public void setProcessOutput(int processOutput) {
        this.processOutput = processOutput;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public Path getFilePath() {
        return filePath;
    }

    public String getName() {
        return name;
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
