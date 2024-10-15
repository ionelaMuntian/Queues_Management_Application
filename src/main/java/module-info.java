module com.example.queues_management_application {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.example.queues_management_application to javafx.fxml;
    exports com.example.queues_management_application.GUI;
    exports com.example.queues_management_application;
    opens com.example.queues_management_application.GUI to javafx.fxml;
    exports com.example.queues_management_application.Model;
    opens com.example.queues_management_application.Model to javafx.fxml;
    exports com.example.queues_management_application.BusinessLogic;
    opens com.example.queues_management_application.BusinessLogic to javafx.fxml;
}