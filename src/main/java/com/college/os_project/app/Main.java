package com.college.os_project.app;

import com.college.os_project.view.ConsoleView;
import com.college.os_project.view.GUIView;
import com.college.os_project.view.View;

public class Main {
    public static void main(String[] args) {
        View guiView = new GUIView();
        View cmdView = new ConsoleView();

        Simulator osSimulator = new Simulator(cmdView);
        osSimulator.run();
    }
}
