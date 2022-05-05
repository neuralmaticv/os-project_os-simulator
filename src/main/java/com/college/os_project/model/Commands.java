package com.college.os_project.model;

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
                    System.out.println("Command '" + inputArr[0] + "' not found, type 'help' to list available commands.\n");
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
                    runProcess(inputArr[1]);
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
                    System.out.println("Command '" + inputArr[0] + "' not found, type 'help' to list available commands.\n");
            }
        } else {
            System.out.println("Command '" + input + "' not found, type 'help' to list available commands.\n");
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

    private static void runProcess(String arg) {
        // TODO:
    }

    private static void stopProcess(Integer pid) {
        // TOODO:
    }

    private static void blockProcess(Integer pid) {
        // TODO:
    }

    private static void unblockProcess(Integer pid) {
        // TODO:
    }

    private static void runAllProcesses() {
        // TODO:
    }

    private static void listAllProcesses() {
        //Process.showAllProcesses();
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
        System.out.println("Bye!");
        System.exit(1);
    }

    private static void help() {
        System.out.printf("%-40s %s", "ls", "List files and directories. [NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "cd <dir name>", "Change working directory. [NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "mkdir <dir name>", "Make a directory. [NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "rm <dir name>", "Remove a directory. [NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "run <program name> <output file>", "[NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "rap", "Run all processes. [NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "stop <process id>", "[NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "block <process id>", "[NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "unblock <process id>", "[NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "ps", "List all processes.  [NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "mem", "Show memory. [NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "clear", "Clear the terminal. [NOT IMPLEMENTED]\n");
        System.out.printf("%-40s %s", "exit", "Terminate simulator.\n");
        System.out.printf("%-40s %s", "help", "List of commands.\n");
    }
}
