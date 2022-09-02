package com.college.os_project.model.memory;

import com.college.os_project.model.filesystem.FileInMemory;

import java.util.ArrayList;

public class SecondaryMemory {
    private static int size;
    public static Block[] blocks;
    private static int numberOfBlocks;
    public static ArrayList<FileInMemory> files;

    public SecondaryMemory() {
        size = 2048;
        numberOfBlocks = size / Block.getSize();
        blocks = new Block[numberOfBlocks];
        for (int i = 0; i < numberOfBlocks; i++) {
            Block newBlock = new Block(i);
            blocks[i] = newBlock;
        }
        files = new ArrayList<>();
    }

    public void save(FileInMemory file) {
        int reminder = file.getSize() % Block.getSize();
        int numOfBlocks;
        if (reminder == 0)
            numOfBlocks = file.getSize() / Block.getSize();
        else
            numOfBlocks = (file.getSize() + 4 - reminder) / Block.getSize();
        numOfBlocks++;

        if (numberOfFreeBlocks() >= numOfBlocks) {
            int counter = 0;
            int listCounter = 0;
            Block indexBlockOfFile = null;
            int helpList[] = null;

            for (int i = 0; i < numberOfBlocks; i++)
                if (!blocks[i].isOccupied()) {
                    blocks[i].setOccupied(true);

                    if (counter == 0) {
                        indexBlockOfFile = blocks[i];
                        file.setIndexBlock(i);
                        blocks[i].setContent("ib  ".getBytes());
                        counter++;
                        helpList = new int[numOfBlocks - 1];
                    } else {
                        helpList[listCounter] = i;
                        blocks[i].setContent(FileInMemory.part(counter - 1));

                        listCounter++;
                        counter++;

                        if (counter == numOfBlocks) {
                            file.setLength(counter);
                            files.add(file);
                            indexBlockOfFile.setList(helpList);
                            return;
                        }

                    }
                }
        } else
            System.out.println("Not enough space, cannot create file ");
    }

    public void deleteFile(FileInMemory file) {
        if (!files.contains(file))
            System.out.println("Your file is not in the secondary memory");
        else {
            int index = file.getIndexBlock();
            Block in = blocks[index];
            for (int i = 0; i < in.getList().length; i++) {
                int index2 = in.getList()[i];
                blocks[index2].setOccupied(false);
                blocks[index2].setContent(null);
            }
            blocks[index].setOccupied(false);
            blocks[index].setContent(null);
        }
        files.remove(file);
    }

    public String readFile(FileInMemory file) {
        String read = "";
        int index = file.getIndexBlock();
        Block in = blocks[index];
        for (int i = 0; i < in.getList().length; i++) {
            int index2 = in.getList()[i];
            System.out.println(index2);
            byte[] content = blocks[index2].getContent();
            for (byte singleByte : content)
                read += (char) singleByte;
        }
        return read;
    }

    private static int numberOfFreeBlocks() {
        int counter = 0;
        for (int i = 0; i < numberOfBlocks; i++)
            if (!blocks[i].isOccupied())
                counter++;
        return counter;
    }


    public boolean contains(String fileName) {
        for (FileInMemory f : files)
            if (f.getName().equals(fileName))
                return true;
        return false;
    }

    public FileInMemory getFile(String fileName) {
        for (FileInMemory f : files)
            if (f.getName().equals(fileName))
                return f;
        return null;
    }

    public static int getSize() {
        return size;
    }

    public static Block[] getBlocks() {
        return blocks;
    }

    public ArrayList<FileInMemory> getFiles() {
        return files;
    }

    private static String printList(int[] list) {
        String list2 = "";
        for (int i = 0; i < list.length; i++) {
            list2 += list[i] + ", ";
        }
        return list2.substring(0, list2.length() - 2);
    }

    public static void printMemoryAllocationTable() {
        System.out.println("Name of file\tIndex block\tBlocks occupied by this file\tLength");
        for (FileInMemory file : files)
            System.out.println(file.getName() + "\t\t" + file.getIndexBlock() + "\t\t" +
                    printList(blocks[file.getIndexBlock()].getList()) + "\t\t" + file.getLength());
    }


}
