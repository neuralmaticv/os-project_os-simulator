package com.college.os_project.model.kernel;

import com.college.os_project.model.Bootloader;
import com.college.os_project.model.memory.Memory;
import com.college.os_project.model.memory.MemoryManager;
import com.college.os_project.model.memory.MemoryPartition;
import java.text.SimpleDateFormat;
import java.time.Duration;
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
    private MemoryPartition partition = null;

    public Process(String username, int priority, int size) {
        this.PID = ProcessScheduler.allProcesses.size();
        this.username = username;
        this.state = ProcessState.READY;
        this.priority = priority;
        this.size = size;
        this.setDaemon(false);

        ProcessScheduler.allProcesses.add(this);
    }

    public Process(String username, int priority, ProcessState ps, int size, boolean isDaemon) {
        this.PID = ProcessScheduler.allProcesses.size();
        this.username = username;
        this.state = ps;
        this.priority = priority;
        this.size = size;

        if (isDaemon) {
            this.setDaemon(true);
            this.startTime = new Date();
            Bootloader.memoryManager.loadProcess(this);
        } else {
            this.setDaemon(false);
        }

        ProcessScheduler.allProcesses.add(this);
    }

    @Override
    public void run() {
        while (!this.getProcessState().equals(ProcessState.TERMINATED)) {
            if (this.getPID() != 0) {
                ProcessScheduler.getProcess(0).incSize(0);
            }

            try {
                if (this.getProcessState().equals(ProcessState.RUNNING)) {
                    this.sleep(10);
                    totalTimeMS += 10;
                } else {
                    this.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void blockProcess() {
        if (PID == 0) {
            System.out.println("You are not allowed to block this process.");
        } else if (this.state.equals(ProcessState.RUNNING)) {
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
        if (this.state.equals(ProcessState.BLOCKED)) {
            this.state = ProcessState.READY;
            System.out.printf("Process with PID = %d is unblocked.\n", PID);
            ProcessScheduler.readyQueue.add(this);
        } else {
            System.out.printf("Process with PID = %d is not blocked.\n", PID);
        }
    }

    public void terminateProcess() {
        if (PID == 0) {
            System.out.println("You are not allowed to terminate this process.");
        } else if (this.state.equals(ProcessState.READY) || this.state.equals(ProcessState.RUNNING)) {
            this.state = ProcessState.TERMINATED;
            MemoryManager.removeProcess(this);
            System.out.printf("Process with PID = %d is terminated.\n", this.getPID());

            if (ProcessScheduler.readyQueue.contains(this)) {
                ProcessScheduler.readyQueue.remove(this);
            }
        } else if (this.state.equals(ProcessState.BLOCKED)) {
            this.state = ProcessState.TERMINATED;
            MemoryManager.removeProcess(this);
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

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getProgramCounter() {
        return this.programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public boolean loadProcess(int address) {
        this.partition = Memory.occupyPartition(address, this);
        if (partition == null) {
            return false;
        } else {
            return true;
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
