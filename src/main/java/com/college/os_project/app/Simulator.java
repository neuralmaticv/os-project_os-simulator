package com.college.os_project.app;

import com.college.os_project.view.View;

public class Simulator {
    private final View view;

    public Simulator(View view) {
        this.view = view;
    }

    public void run() {
        view.drawView();
    }
}
