package com.example.queues_management_application.GUI;
import com.example.queues_management_application.BusinessLogic.SimulationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Button;

public class SimulationFrame extends Application {
    private HelloController helloController;
    private ControllerQueueEvolution controllerQueueEvolution;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimulationFrame.class.getResource("setup.fxml"));
        Parent root = fxmlLoader.load();

        helloController = fxmlLoader.getController();
        helloController.setApplication(this); // Set the application reference in the controller

        // stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 815, 609));
        stage.show();
    }
    public void queueEvolutionStage(int Q, int N) throws IOException {
        Stage secondStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("queue_evolution.fxml"));
        loader.setControllerFactory(controllerClass -> {
            controllerQueueEvolution = new ControllerQueueEvolution();
            controllerQueueEvolution.setApplication(SimulationFrame.this);
            return controllerQueueEvolution;
        });
        Parent root = loader.load();

        secondStage.setScene(new Scene(root, 815, 609));
        secondStage.show();

        controllerQueueEvolution.addRectangles(Q);
    }
    public HelloController getHelloController() {
        return helloController;
    }
    public ControllerQueueEvolution getControllerQueueEvolution() {
        return controllerQueueEvolution;
    }
    public static void main(String[] args) throws IOException {
        launch(args);
    }
}