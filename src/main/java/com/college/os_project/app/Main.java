package com.college.os_project.app;

import com.college.os_project.view.*;

public class Main {
    public static void main(String[] args) {
        View guiView = new GUIView();
        View cmdView = new ConsoleView();

        Simulator osSimulator = new Simulator(cmdView);
        osSimulator.run();
    }
}
