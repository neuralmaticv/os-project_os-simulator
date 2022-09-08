package com.college.os_project.model.memory;

import com.college.os_project.model.processor.Process;

import java.util.ArrayList;

public class Memory {
    private static int MAX_CAPACITY;
    private static int occupied;
    private static final ArrayList<MemoryPartition> partitions = new ArrayList<>();

    public static void init(int size) {
        MAX_CAPACITY = size;
        occupied = 0;
        partitions.add(new MemoryPartition(size));
    }
    
    public static MemoryPartition occupyPartition(MemoryPartition partition, Process process) {
        if (!partitions.contains(partition)) {
            return null;
        } else {
            return partition.occupyMemory(process);
        }
    }

    public static boolean mergePartitions(int address) {
        if (address > 0 && partitions.get(address - 1).getProcess() == null) {
            address--;
        }

        if (address >= partitions.size() - 1 || partitions.get(address + 1).getProcess() != null) {
            return false;
        }

        MemoryPartition newPartition = new MemoryPartition(partitions.get(address), partitions.get(address + 1));
        partitions.set(address, newPartition);
        partitions.remove(address + 1);

        boolean flag = true;
        while (flag) {
            flag = mergePartitions(address);
        }

        return true;
    }

    public static boolean separatePartitions(MemoryPartition partition) {
        int location = partitions.indexOf(partition);
        return separatePartitions(location);
    }

    public static boolean separatePartitions(int address) {
        MemoryPartition partition = partitions.get(address);

        if (partition.getSize() == partition.getOccupied()) {
            return false;
        }

        MemoryPartition newPartition1 = new MemoryPartition(partition.getOccupied());
        newPartition1.occupyMemory(partition.getProcess());
        MemoryPartition newPartition2 = new MemoryPartition(partition.getFreeSpace());

        partitions.set(address, newPartition1);
        partitions.add(address + 1, newPartition2);
        mergePartitions(address + 1);

        return true;
    }

    public static int getCapacity() {
        return MAX_CAPACITY;
    }

    public static int getOccupiedSpace() {
        occupied = 0;

        for (MemoryPartition p : partitions) {
            occupied += p.getOccupied();
        }

        return occupied;
    }

    public static int getFreeSpace() {
        return MAX_CAPACITY - occupied;
    }

    public static ArrayList<MemoryPartition> getPartitions() {
        return partitions;
    }

    public static ArrayList<MemoryPartition> getFreePartitions(int size) {
        ArrayList<MemoryPartition> freePartitions = new ArrayList<>();

        for (MemoryPartition mp : partitions) {
            if (mp.getProcess() == null && mp.getSize() >= size) {
                freePartitions.add(mp);
            }
        }

        return freePartitions;
    }

    public static ArrayList<MemoryPartition> getSuitablePartitions(int size) {
        ArrayList<MemoryPartition> suitablePartitions = new ArrayList<>();

        for (MemoryPartition mp : partitions) {
            if (mp.getSize() >= size) {
                suitablePartitions.add(mp);
            }
        }

        return suitablePartitions;
    }

    public static void info() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-10s %-5s\t\t %-5s\t\t %-5s", "", "TOTAL", "USED", "FREE")).append("\n");
        sb.append(String.format("%-10s %-5d\t\t %-5d\t\t %-5d", "MEM:", MAX_CAPACITY, getOccupiedSpace(), getFreeSpace()));
        sb.append("\n");

        for (MemoryPartition p : partitions) {
            sb.append(p);
        }

        System.out.println(sb);
    }
}
