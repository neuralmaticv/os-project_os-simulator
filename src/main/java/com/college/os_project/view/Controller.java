package com.college.os_project.view;

import com.college.os_project.model.Bootloader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textInputField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String hostname = "UNKNOWN HOST NAME";
        System.out.println(hostname);

        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Operating System Simulator").append("[Version 0.0.1]").append("\n");
        sb.append("Authors: ").append("Vladimir Mijic").append("\n");
        sb.append("On computer: ").append(hostname).append("\n");
        sb.append("Type `help' to see this list of commands.").append("\n");
        sb.append("-------------------------------------------------------------------\n\n");

        textArea.setText(sb.toString());

        try {
            Bootloader.boot();
            System.out.println("Successfully booted.");
        } catch (IOException e) {
            System.out.println("Boot error occured.");
            return;
        }
    }
}
