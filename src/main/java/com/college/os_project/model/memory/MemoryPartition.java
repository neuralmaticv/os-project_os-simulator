package com.college.os_project.model.memory;

import com.college.os_project.model.processor.Process;

public class MemoryPartition {
    private Process process;
    private final int size;
    private int occupied;

    public MemoryPartition(int size) {
        this.size = size;
        this.process = null;
        occupied = 0;
    }

    public MemoryPartition(MemoryPartition mp1, MemoryPartition mp2) {
        this.size = mp1.size + mp2.size;
        occupied = 0;
    }

    public MemoryPartition occupyMemory(Process process) {
        freeMemory();

        if (process.getSize() > size) {
            return null;
        } else {
            this.process = process;
            this.occupied = process.getSize();
            return this;
        }
    }

    public void freeMemory() {
        if (this.process != null) {
            this.process = null;
            this.occupied = 0;
        }
    }

    public Process getProcess() {
        return process;
    }

    public int getSize() {
        return size;
    }

    public int getOccupied() {
        return occupied;
    }

    public int getFreeSpace() {
        return size - occupied;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.process == null) {
            sb.append(String.format(" [   | %4d of %4d] ", this.occupied, this.size));
        } else {
            sb.append(String.format(" [%3s | %4d of %4d] ", this.process.getPID(), this.occupied, this.size));
        }

        return sb.toString();
    }
}
