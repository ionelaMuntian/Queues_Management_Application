package com.example.queues_management_application.GUI;

import com.example.queues_management_application.BusinessLogic.SimulationManager;
import com.example.queues_management_application.Model.Server;
import com.example.queues_management_application.Model.Task;
import com.example.queues_management_application.BusinessLogic.SelectionPolicy;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerQueueEvolution {


    @FXML
    private Label avgServiceTime;

    @FXML
    private Label peakHour;

    @FXML
    private Label avgWaitingTime;

    @FXML
    private Label counterLabel;

    @FXML
    private Button QueueStrategy;

    @FXML
    private Button TimeStrategy;
    private SelectionPolicy strategy = SelectionPolicy.SHORTEST_TIME;
    @FXML
    private Label Q;
    @FXML
    private AnchorPane Container;
    @FXML
    public Rectangle Queue1;
    @FXML
    private ImageView figure;

    @FXML
    private Button start;
    private SimulationFrame application;

    // Lists to store references to rectangles/buttons
    private List<Rectangle> rectangleList = new ArrayList<>();

    // Map to associate each task with its corresponding labels
    private Map<Task, Label> arrivalLabelsMap = new HashMap<>();
    private Map<Task, Label> serviceLabelsMap = new HashMap<>();
    private Map<Task, Label> waitingTimeLabelsMap = new HashMap<>();

    // Map to associate each task with its corresponding image view
    private Map<Task, ImageView> figureViewsMap = new HashMap<>();

    public void setApplication(SimulationFrame application) {
        this.application = application;
    }

    @FXML
    void QueueStrategy_onAction(ActionEvent event) {
        strategy = SelectionPolicy.SHORTEST_QUEUE;
    }

    @FXML
    void TimeStrategy_onAction(ActionEvent event) {
        strategy = SelectionPolicy.SHORTEST_TIME;
    }

    @FXML
    void start_onAction(ActionEvent event) {
        figure.setVisible(true);
        SimulationManager simulationManager = new SimulationManager(application);
        Thread t = new Thread(simulationManager);
        t.start();
    }

    public void addRectangles(int numberOfRectangles) {
        double initialX = 41; // Initial X position
        double initialY = 110; // Initial Y position

        double rectangleWidth = 121; // Width of each rectangle
        double rectangleHeight = 40; // Height of each rectangle
        double verticalSpacing = 100; // Vertical spacing between rectangles

        // Loop to create and add rectangles/buttons
        for (int i = 0; i < numberOfRectangles; i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(rectangleWidth);
            rectangle.setHeight(rectangleHeight);
            rectangle.setX(initialX);
            rectangle.setY(initialY + i * verticalSpacing); // Adjust Y position
            rectangle.setStyle("-fx-fill: #00694d; -fx-stroke: black;"); // Set style

            // Add label
            Label label = new Label("Queue" + (i + 1));
            label.setLayoutX(initialX + 20); // Adjust X position of the label
            label.setLayoutY(initialY + i * verticalSpacing + rectangleHeight / 2 - 10); // Adjust Y position of the label to be centered vertically
            label.setFont(Font.font("Times New Roman", 20));
            label.setStyle("-fx-text-fill: white;"); // Set text color

            rectangleList.add(rectangle);
            Container.getChildren().addAll(rectangle, label);
        }
    }

    public void addFigures(Task task) {
        Platform.runLater(() -> {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(38);
            imageView.setFitHeight(88);
            imageView.setX(733);
            imageView.setY(100 + task.getID() * 200 + 10); // Center vertically
            imageView.setImage(figure.getImage());

            Container.getChildren().add(imageView);

            // Add image view to map
            figureViewsMap.put(task, imageView);
        });
    }


    public void addLabels(Task task) {
        Platform.runLater(() -> {
            double verticalSpacing = 200; // Increased vertical spacing between figures
            Label arrivalLabel = new Label("Arrival: " + task.getArrivalTime()); // Add text to the arrival label
            arrivalLabel.setLayoutX(733);
            arrivalLabel.setLayoutY(100 + task.getID() * verticalSpacing + 90); // Adjusted Y position for arrival label
            arrivalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            arrivalLabel.setTextAlignment(TextAlignment.CENTER);

            Label serviceLabel = new Label("Service: " + task.getServiceTime());
            serviceLabel.setLayoutX(733);
            serviceLabel.setLayoutY(100 + task.getID() * verticalSpacing + 110); // Adjusted Y position for service label
            serviceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            serviceLabel.setTextAlignment(TextAlignment.CENTER);

            Label waitingTimeLabel = new Label("Waiting: "); // Create waiting time label
            waitingTimeLabel.setLayoutX(733);
            waitingTimeLabel.setLayoutY(100 + task.getID() * verticalSpacing + 130); // Adjusted Y position for waiting time label
            waitingTimeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            waitingTimeLabel.setTextAlignment(TextAlignment.CENTER);

            Container.getChildren().addAll(arrivalLabel, serviceLabel, waitingTimeLabel);

            // Add labels to maps
            arrivalLabelsMap.put(task, arrivalLabel);
            serviceLabelsMap.put(task, serviceLabel);
            waitingTimeLabelsMap.put(task, waitingTimeLabel);
        });
    }


    public void updateLabels(Task task, int arrivalText, int serviceText, int waitingTime) {
        Platform.runLater(() -> {
            // Get labels from map
            Label arrivalLabel = arrivalLabelsMap.get(task);
            Label serviceLabel = serviceLabelsMap.get(task);
            Label waitingTimeLabel = waitingTimeLabelsMap.get(task);

            // Update labels with new values
            arrivalLabel.setText("Arrival: " + arrivalText);
            serviceLabel.setText("Service: " + serviceText);
            waitingTimeLabel.setText("Waiting: " + waitingTime);
        });
    }

    // Other methods...
    public void moveFigure(Task task, int indexServer, int nrTasksInServer) {
        Platform.runLater(() -> {
            Label arrivalLabel = arrivalLabelsMap.get(task);
            Label serviceLabel = serviceLabelsMap.get(task);
            Label waitingTimeLabel = waitingTimeLabelsMap.get(task);
            ImageView imageView = figureViewsMap.get(task);
            double rectangleWidth = 121;
            double rectangleHeight = 40;


            if (arrivalLabel != null && serviceLabel != null && waitingTimeLabel != null && imageView != null) {
                double initialX = 41;
                double initialY = 100;
                double verticalSpacing = 100;
                double figureSpacing = 10;

                double xPosition = initialX + rectangleWidth + 20 + (nrTasksInServer - 1) * 150;
                double figureYPosition = initialY + indexServer * verticalSpacing + figureSpacing;

                arrivalLabel.setLayoutX(xPosition);
                arrivalLabel.setLayoutY(figureYPosition);
                serviceLabel.setLayoutX(xPosition);
                serviceLabel.setLayoutY(figureYPosition + arrivalLabel.getHeight());
                waitingTimeLabel.setLayoutX(xPosition);
                waitingTimeLabel.setLayoutY(figureYPosition + arrivalLabel.getHeight() + serviceLabel.getHeight());

                imageView.setX(xPosition + arrivalLabel.getWidth());
                imageView.setY(figureYPosition);
            } else {
                System.out.println("Invalid task in moveFigure!");
            }
        });
    }

    public void removeFigure(Task task) {
        Platform.runLater(() -> {
            Label arrivalLabel = arrivalLabelsMap.remove(task);
            Label serviceLabel = serviceLabelsMap.remove(task);
            Label waitingTimeLabel = waitingTimeLabelsMap.remove(task);
            ImageView imageView = figureViewsMap.remove(task);

            if (arrivalLabel != null && serviceLabel != null && waitingTimeLabel != null && imageView != null) {
                Container.getChildren().removeAll(arrivalLabel, serviceLabel, waitingTimeLabel, imageView);
            } else {
                System.out.println("Invalid task in removeFigure!");
            }
        });
    }

    public void updatePositionsInQueue(Server server) {
        Platform.runLater(() -> {
            double initialX = 41;
            double rectangleWidth = 121;
            int i = 0;
            for (Task task : server.getTasksList()) {
                if (i >= server.getTasksList().size()) break;

                double xPosition = initialX + rectangleWidth + 20 + i * 150;
                Label arrivalLabel = arrivalLabelsMap.get(task);
                Label serviceLabel = serviceLabelsMap.get(task);
                Label waitingTimeLabel = waitingTimeLabelsMap.get(task);
                ImageView imageView = figureViewsMap.get(task);

                if (arrivalLabel != null && serviceLabel != null && waitingTimeLabel != null && imageView != null) {
                    arrivalLabel.setLayoutX(xPosition);
                    serviceLabel.setLayoutX(xPosition);
                    waitingTimeLabel.setLayoutX(xPosition);
                    imageView.setX(xPosition + arrivalLabel.getWidth());
                    i++;
                }
            }
        });
    }

    public void setWaitingTime(Task task, int time) {
        Platform.runLater(() -> {
            Label waitingTimeLabel = waitingTimeLabelsMap.get(task);
            if (waitingTimeLabel != null) {
                waitingTimeLabel.setText("Waiting: " + time);
            } else {
                System.out.println("Invalid task in setWaitingTime!");
            }
        });
    }

    public List<Rectangle> getRectangleList() {
        return rectangleList;
    }

    public SelectionPolicy getStrategy() {
        return strategy;
    }

    public Label getCounterLabel() {
        return counterLabel;
    }

    public void setCounterLabel(int time) {
        Platform.runLater(() -> {
            this.counterLabel.setText(String.valueOf(time));
        });
    }

    public void setAvgWaitingTime(float avgWaitingTime) {
        Platform.runLater(() -> {
            this.avgWaitingTime.setText(String.valueOf(avgWaitingTime));
        });
    }

    public void setAvgServiceTime(float avgServiceTime) {
        Platform.runLater(() -> {
            this.avgServiceTime.setText(String.valueOf(avgServiceTime));
        });
    }

    public void setPeakHour(int peakHour) {
        Platform.runLater(() -> {
            this.peakHour.setText(String.valueOf(peakHour));
        });
    }
}