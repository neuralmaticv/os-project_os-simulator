module com.college.osproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.college.os_project.app to javafx.fxml;
    exports com.college.os_project.app;

//    opens com.college.os_project.model to javafx.fxml;
//    exports com.college.os_project.model;

    opens com.college.os_project.view to javafx.fxml;
    exports com.college.os_project.view;
}