package com.example.queues_management_application.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
public class HelloController {
    private Integer numberClients;
    private Integer numberQueues;
    private Integer maxTA;
    private Integer minTA;
    private Integer maxTS;
    private Integer minTS;
    private Integer timeMaxSim;

    @FXML
    private Label errorLabel;
    @FXML
    private TextField maxSt;

    @FXML
    private TextField maxTa;

    @FXML
    private TextField minSt;

    @FXML
    private TextField minTa;

    @FXML
    private TextField nrClients;

    @FXML
    private TextField nrQueues;

    @FXML
    private TextField simulationInterval;

    @FXML
    private Button validateInput;

    @FXML
    private Label welcomeText;
    private SimulationFrame application;

    // Add a setApplication method
    public void setApplication(SimulationFrame application) {
        this.application = application;
    }

    @FXML
    public void  validateInput_onAction(ActionEvent event) throws IOException {
        numberClients = Integer.parseInt(nrClients.getText());
        numberQueues = Integer.parseInt(nrQueues.getText());
        maxTA = Integer.parseInt(maxTa.getText());
        minTA = Integer.parseInt(minTa.getText());
        maxTS = Integer.parseInt(maxSt.getText());
        minTS = Integer.parseInt(minSt.getText());
        timeMaxSim = Integer.parseInt(simulationInterval.getText());

        if (maxTA < minTA) {
            errorLabel.setText("The maximum time arrival must be greater than minimum time arrival!");
        } else {
            if (maxTS < minTS) {
                errorLabel.setText("The maximum time service must be greater than minimum time service!");
            } else {
                if (timeMaxSim <= 0) {
                    errorLabel.setText("The simulation time must be greater than 0!");
                } else {
                    if (numberQueues <= 0) {
                        errorLabel.setText("The number of queues must be greater than 0!");
                    } else {
                        if (numberClients <= 0) {
                            errorLabel.setText("The number of clients must be greater than 0!");
                        } else {
                            if (maxTA > timeMaxSim) {
                                errorLabel.setText("Clients must arrive before the maximum simulation time!");
                            }
                            application.queueEvolutionStage(numberQueues,numberClients);
                        }
                    }
                }
            }
        }
    }
    public Integer getNumberClients() {
        return numberClients;
    }

    public Integer getNumberQueues() {
        return numberQueues;
    }

    public Integer getMaxTA() {
        return maxTA;
    }

    public Integer getMinTA() {
        return minTA;
    }

    public Integer getMaxTS() {
        return maxTS;
    }

    public Integer getMinTS() {
        return minTS;
    }

    public Integer getTimeMaxSim() {
        return timeMaxSim;
    }
}