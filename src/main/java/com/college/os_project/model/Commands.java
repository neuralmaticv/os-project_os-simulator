package com.college.os_project.model;

import com.college.os_project.model.filesystem.FileInMemory;
import com.college.os_project.model.filesystem.FileSystem;
import com.college.os_project.model.memory.Memory;
import com.college.os_project.model.memory.SecondaryMemory;
import com.college.os_project.model.processor.Process;
import com.college.os_project.model.processor.ProcessScheduler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;

public class Commands {
    public static void checkCommand(String input) {
        String[] inputArr = input.split(" ");

        if (inputArr.length == 1) {
            switch (inputArr[0]) {
                case "ls" -> listFilesAndDirs();
                case "ps" -> listAllProcesses();
                case "clear" -> clearTerminal();
                case "exit" -> exit();
                case "help" -> help();
                default ->
                        System.out.println("Command '" + inputArr[0] + "' not found, type 'help' to list available commands.");
            }
        } else if (inputArr.length == 2) {
            switch (inputArr[0]) {
                case "cd":
                    changeWorkingDir(inputArr[1]);
                    break;
                case "mkdir":
                    makeDir(inputArr[1]);
                    break;
                case "rmd":
                    removeDir(inputArr[1]);
                    break;
                case "rmf":
                    removeFile(inputArr[1]);
                    break;
                case "run":
                    try {
                        int processID = Integer.parseInt(inputArr[1]);
                        runProcess(processID);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid parameter, please provide process ID.");
                        break;
                    }
                case "load":
                    if (inputArr[1].endsWith(".asm")) {
                        loadProcess(inputArr[1].split(".asm")[0], inputArr[1], null);
                        break;
                    }
                case "stop":
                    stopProcess(Integer.valueOf(inputArr[1]));
                    break;
                case "block":
                    blockProcess(Integer.valueOf(inputArr[1]));
                    break;
                case "unblock":
                    unblockProcess(Integer.valueOf(inputArr[1]));
                    break;
                case "mem":
                    if (inputArr[1].equals("-m")) {
                        showMainMemory();
                        break;
                    } else if (inputArr[1].equals("-s")) {
                        showSecondaryMemory();
                        break;
                    } else {
                        System.out.println("Invalid parameter.");
                        break;
                    }
                default:
                    System.out.println("Command '" + inputArr[0] + " " + inputArr[1] + "' not found, type 'help' to list available commands.");
            }
        } else if (inputArr.length == 3) {
            if (inputArr[0].equals("rename")) {
                renameDir(inputArr[1], inputArr[2]);
            } else if (inputArr[0].equals("load")) {
                if (inputArr[1].endsWith(".asm")) {
                    loadProcess(inputArr[1].split(".asm")[0], inputArr[1], inputArr[2]);
                }
            } else {
                System.out.println("Invalid parameters or command.");
            }
        } else {
            System.out.println("Command '" + input + "' not found, type 'help' to list available commands.");
        }
    }

    private static void listFilesAndDirs() {
        FileSystem.listFiles();
    }

    private static void changeWorkingDir(String arg) {
        FileSystem.changeDirectory(arg);
    }

    private static void makeDir(String arg) {
        FileSystem.makeDirectory(arg);
    }

    private static void removeDir(String arg) {
        FileSystem.deleteDirectory(arg);
    }

    private static void removeFile(String arg) {
        FileSystem.deleteFile(arg);
    }

    private static void renameDir(String newName, String oldName) {
        FileSystem.renameDirectory(newName, oldName);
    }

    private static void loadProcess(String processName, String fullFileName, String outputFileName) {
        try {
            Random random = new Random();
            new Process(processName, fullFileName, outputFileName, random.nextInt(6) + 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runProcess(Integer PID) {
        Process p = ProcessScheduler.getProcess(PID);

        if (p != null) {
            new ProcessScheduler().start();
        } else {
            System.out.printf("Process with PID = %d does not exist.\n", PID);
        }
    }

    private static void stopProcess(Integer PID) {
        ProcessScheduler.stopProcess(PID);
    }

    private static void blockProcess(Integer PID) {
        ProcessScheduler.blockProcess(PID);
    }

    private static void unblockProcess(Integer PID) {
        ProcessScheduler.unblockProcess(PID);
    }

    private static void listAllProcesses() {
        ProcessScheduler.showAllProcesses();
    }

    private static void showMainMemory() {
        Memory.info();
    }
    private static void showSecondaryMemory() {
        SecondaryMemory.printMemoryAllocationTable();
    }

    private static void clearTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void exit() {
        System.out.println("Bye!");
        System.exit(1);
    }

    private static void help() {
        System.out.printf("%-40s %s\n", "ls", "List files and directories.");
        System.out.printf("%-40s %s\n", "cd <dir name>", "Change working directory.");
        System.out.printf("%-40s %s\n", "mkdir <dir name>", "Make a directory.");
        System.out.printf("%-40s %s\n", "rmd <dir name>", "Remove a directory.");
        System.out.printf("%-40s %s\n", "rmf <file name>", "Remove a file.");
        System.out.printf("%-40s %s\n", "rename <new name> <old name>", "Rename a directory or file.");
        System.out.printf("%-40s %s\n", "load <program name>", "Load a process.");
        System.out.printf("%-40s %s\n", "load <program name> <output file>", "Run process and store result to output file.");
        System.out.printf("%-40s %s\n", "run <process id>", "Run process with specific PID.");
        System.out.printf("%-40s %s\n", "stop <process id>", "Stop process with specific PID.");
        System.out.printf("%-40s %s\n", "block <process id>", "Block process with specific PID.");
        System.out.printf("%-40s %s\n", "unblock <process id>", "Unblock process with specific PID.");
        System.out.printf("%-40s %s\n", "ps", "List all processes.");
        System.out.printf("%-40s %s\n", "mem -m", "Show main memory.");
        System.out.printf("%-40s %s\n", "mem -s", "Show secondary memory.");
        System.out.printf("%-40s %s\n", "clear", "Clear the terminal.");
        System.out.printf("%-40s %s\n", "exit", "Terminate simulator.");
        System.out.printf("%-40s %s\n", "help", "List of commands.");
    }

    public static void setOutput(OutputStream output) {
        System.setOut(new PrintStream(output, true));
    }
}
