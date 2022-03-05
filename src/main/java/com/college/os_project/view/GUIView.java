package com.college.os_project.view;

import com.college.os_project.app.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIView extends Application implements View {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/college/os_project/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        scene.getStylesheets().add(Main.class.getResource("/com/college/os_project/style.css").toExternalForm());
        stage.setTitle("OS Simulator");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void drawView() {
        launch();
    }
}
