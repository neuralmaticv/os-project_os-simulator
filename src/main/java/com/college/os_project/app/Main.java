package com.college.os_project.app;

import com.college.os_project.view.ConsoleView;
import com.college.os_project.view.GUIView;

import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        System.out.println("Select view: command-line interface (CMD) or graphical user interface (GUI)");
        sb.append("Enter:").append("\n");
        sb.append("\t0 for CMD View or\n");
        sb.append("\t1 for GUI View [suggested]\n");
        int userInput;

        do {
            System.out.println(sb);
            System.out.print("[?] view: ");
            userInput = scanner.nextInt();
        } while(userInput != 0 && userInput != 1);

        Simulator osSimulator;

        if (userInput == 0) {
            osSimulator = new Simulator(new ConsoleView());
        } else {
            osSimulator = new Simulator(new GUIView());
        }

        osSimulator.run();
        scanner.close();
    }
}
