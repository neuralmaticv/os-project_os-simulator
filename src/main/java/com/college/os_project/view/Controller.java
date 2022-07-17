package com.college.os_project.view;

import com.college.os_project.model.Bootloader;
import com.college.os_project.model.Commands;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private String hostname;
    @FXML
    public TextArea textArea;
    @FXML
    public TextField textInputField;
    private final PipedInputStream input = new PipedInputStream();
    private final PipedOutputStream output = new PipedOutputStream();
    private StringBuilder outputStringBuilder = new StringBuilder();
    private OutputStream outputStream;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            input.connect(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStringBuilder.append((char) b);
                if (((char) b) == '\n') {
                    displayText();
                }
            }
        };
        Commands.setOutput(outputStream);

        try {
            Bootloader.boot();
            hostname = (InetAddress.getLocalHost().getHostName());
            System.out.println("Successfully booted.\n");
            displayInfo();
        } catch (IOException e) {
            System.out.println("Boot error occured.");
            System.exit(1);
        }

        readUserInput();
    }

    private void readUserInput() {
        textInputField.setOnAction(actionEvent -> {
            String string = "os-simulator:~$ "  + textInputField.getText() + "\n";

            textArea.appendText(string);
            if (textInputField.getText().equals("clear")) {
                clearTerminal();
            } else {
                Commands.checkCommand(textInputField.getText());
            }

            textInputField.clear();
        });
    }

    private void displayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------------------------------------------------------------------").append("\n");
        sb.append("Operating System Simulator").append("[Version 0.0.1]").append("\n");
        sb.append("Authors: ").append("Vladimir Mijic").append("\n");
        sb.append("On computer: ").append(hostname).append("\n");
        sb.append("-------------------------------------------------------------------");
        System.out.println(sb);
    }

    public void displayText() {
        if (!outputStringBuilder.isEmpty()) {
            textArea.appendText(outputStringBuilder.toString());
            outputStringBuilder = new StringBuilder();
        }
    }

    private void clearTerminal() {
        textArea.setText("");
        textInputField.setText("");
    }
}