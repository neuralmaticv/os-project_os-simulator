package com.college.os_project.model.memory;

import com.college.os_project.model.processor.Process;

import java.util.ArrayList;

public class MemoryManager {
    public MemoryManager(int size) {
        Memory.init(size);
    }

    public boolean loadProcess(Process process) {
        int minDiff = Memory.getCapacity();
        int address = 0;
        int processSize = process.getSize();

        ArrayList<MemoryPartition> freePartitions = Memory.getFreePartitions(processSize);
        int fpSize = freePartitions.size();
        for (int i = 0; i < fpSize; i++) {
            if (freePartitions.get(i).getSize() - processSize < minDiff) {
                address = i;
                minDiff = freePartitions.get(i).getSize() - processSize;
            }
        }

        if (fpSize > 0) {
            process.loadProcess(freePartitions.get(address));
            Memory.separatePartitions(freePartitions.get(address));
            return true;
        }

        ArrayList<MemoryPartition> suitablePartitions = Memory.getSuitablePartitions(processSize);
        int spSize = suitablePartitions.size();

        for (int i = 0; i < spSize; i++) {
            if (suitablePartitions.get(i).getSize() - processSize < minDiff) {
                address = i;
                minDiff = suitablePartitions.get(i).getSize() - processSize;
            }
        }

        if (spSize > 0) {
            process.loadProcess(suitablePartitions.get(address));
            Memory.separatePartitions(suitablePartitions.get(address));
            return true;
        }

        return false;
    }

    public static void removeProcess(Process process) {
        for (MemoryPartition mp: Memory.getPartitions()) {
            if (mp.getProcess() != null && mp.getProcess().equals(process)) {
                mp.freeMemory();
                return;
            }
        }
    }
}
