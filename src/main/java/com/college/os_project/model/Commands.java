package com.college.os_project.model;

import com.college.os_project.model.kernel.Process;

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
                case "rap":
                    runAllProcesses();
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
                case "run":
                    runProcess(Integer.valueOf(inputArr[1]));
                    break;
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
                    System.out.println("Command '" + inputArr[0] + "' not found, type 'help' to list available commands.");
            }
        } else {
            System.out.println("Command '" + input + "' not found, type 'help' to list available commands.");
        }
    }

    private static void listFilesAndDirs() {
        // TODO:
    }

    private static void changeWorkingDir(String arg) {
        // TODO:
    }

    private static void makeDir(String arg) {
        // TODO:
    }

    private static void removeDir(String arg) {
        // TODO:
    }

    private static void runProcess(Integer PID) {
        Process p = Process.getProcess(PID);

        if (p != null) {
            p.runProcess();
        } else {
            System.out.printf("Process with PID = %d does not exist.\n", PID);
        }
    }

    private static void stopProcess(Integer PID) {
        Process p = Process.getProcess(PID);

        if (p != null) {
            p.terminateProcess();
        } else {
            System.out.printf("Process with PID = %d does not exist.\n", PID);
        }
    }

    private static void blockProcess(Integer PID) {
        Process p = Process.getProcess(PID);

        if (p != null) {
            p.blockProcess();
        } else {
            System.out.printf("Process with PID = %d does not exist.\n", PID);
        }
    }

    private static void unblockProcess(Integer PID) {
        Process p = Process.getProcess(PID);

        if (p != null) {
            p.unblockProcess();
        } else {
            System.out.printf("Process with PID = %d does not exist.\n", PID);
        }

    }

    private static void runAllProcesses() {
        // TODO:
    }

    private static void listAllProcesses() {
        Process.showAllProcesses();
    }

    private static void showMemory() {
        // TODO:
    }

    private static void clearTerminal() {
        // TODO:
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void exit() {
        stopProcess(0);
        System.out.println("Bye!");
        System.exit(1);
    }

    private static void help() {
        System.out.printf("%-40s %s\n", "ls", "List files and directories. [NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "cd <dir name>", "Change working directory. [NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "mkdir <dir name>", "Make a directory. [NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "rm <dir name>", "Remove a directory. [NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "run <program name> <output file>", "[NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "run <<process id>", "Run process with specific PID.");
        System.out.printf("%-40s %s\n", "stop <process id>", "Stop process with specific PID.");
        System.out.printf("%-40s %s\n", "block <process id>", "Block process with specific PID.");
        System.out.printf("%-40s %s\n", "unblock <process id>", "Unblock process with specific PID.");
        System.out.printf("%-40s %s\n", "rap", "Run all processes.");
        System.out.printf("%-40s %s\n", "ps", "List all processes.");
        System.out.printf("%-40s %s\n", "mem", "Show memory. [NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "clear", "Clear the terminal. [NOT IMPLEMENTED]");
        System.out.printf("%-40s %s\n", "exit", "Terminate simulator.");
        System.out.printf("%-40s %s\n", "help", "List of commands.");
    }
}
