package com.college.os_project.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textInputField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textArea.setText("Welcome!!!");
    }
}
