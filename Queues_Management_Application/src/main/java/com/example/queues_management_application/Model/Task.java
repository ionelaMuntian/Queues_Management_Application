package com.example.queues_management_application.Model;

import com.example.queues_management_application.GUI.ControllerQueueEvolution;
import javafx.scene.control.Label;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private int ID;
    private int arrivalTime;
    private int serviceTime;
    private AtomicInteger waitingTime;   //this must be decremented
    private int waitingTimeFinal;   //this must be decremented

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.waitingTime = new AtomicInteger(0);
        this.waitingTimeFinal = 0;
            }

    public void setWaitingTime(AtomicInteger waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getID() {
        return ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public AtomicInteger getWaitingTime() {
        return waitingTime;
    }

    public int getWaitingTimeFinal() {
        return waitingTimeFinal;
    }

    public void setWaitingTimeFinal(int waitingTimeFinal) {
        this.waitingTimeFinal = waitingTimeFinal;
    }
}

