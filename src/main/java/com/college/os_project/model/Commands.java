package com.college.os_project.model;

import com.college.os_project.model.filesystem.FileSystem;
import com.college.os_project.model.memory.Memory;
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
                case "ls":
                    listFilesAndDirs();
                    break;
                case "ps":
                    listAllProcesses();
                    break;
                case "mem":
                    showMemory();
                    break;
                case "clear":
                    clearTerminal();
                    break;
                case "exit":
                    exit();
                    break;
                case "help":
                    help();
                    break;
                default:
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
                case "rm":
                    removeDir(inputArr[1]);
                    break;
                case "rename":
                    renameDir(inputArr[1], inputArr[2]);
                    break;
                case "run":
                    runProcess(Integer.valueOf(inputArr[1]));
                    break;
                case "load":
                    if (inputArr[1].endsWith(".asm")) {
                        loadProcess(inputArr[1].split(".asm")[0], inputArr[1]);
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
                default:
                    System.out.println("Command '" + inputArr[0] + " " + inputArr[1] + "' not found, type 'help' to list available commands.");
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

    private static void renameDir(String newName, String oldName) {
        FileSystem.renameDirectory(oldName, newName);
    }

    private static void loadProcess( String processName, String fullFileName) {
        try {
            Random random = new Random();
            new Process(processName, fullFileName, random.nextInt(6) + 1);
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

    private static void showMemory() {
        Memory.info();
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
        System.out.printf("%-40s %s\n", "ls", "List files and directories. [NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "cd <dir name>", "Change working directory. [NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "mkdir <dir name>", "Make a directory. [NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "rm <dir name>", "Remove a directory. [NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "rename <new name> <dir name>", "Rename a directory. [NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "run <program name> -o <output file>", "[NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "run <process id>", "Run process with specific PID.");
        System.out.printf("%-40s %s\n", "stop <process id>", "Stop process with specific PID.");
        System.out.printf("%-40s %s\n", "block <process id>", "Block process with specific PID.");
        System.out.printf("%-40s %s\n", "unblock <process id>", "Unblock process with specific PID.");
        System.out.printf("%-40s %s\n", "ps", "List all processes.");
        System.out.printf("%-40s %s\n", "mem", "Show memory.");
        System.out.printf("%-40s %s\n", "clear", "Clear the terminal.");
        System.out.printf("%-40s %s\n", "exit", "Terminate simulator.");
        System.out.printf("%-40s %s\n", "help", "List of commands.");
    }

    public static void setOutput(OutputStream output) {
        System.setOut(new PrintStream(output, true));
    }
}
