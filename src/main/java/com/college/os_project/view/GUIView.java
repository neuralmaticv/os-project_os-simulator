package com.college.os_project.view;

import com.college.os_project.app.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUIView extends Application implements View {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/college/os_project/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1400, 800);
            scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/com/college/os_project/style.css")).toExternalForm());
            stage.setTitle("OS Simulator");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void drawView() {
        launch();
    }
}
