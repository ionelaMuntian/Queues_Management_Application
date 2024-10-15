package com.example.queues_management_application.BusinessLogic;

import com.example.queues_management_application.GUI.ControllerQueueEvolution;
import com.example.queues_management_application.GUI.HelloController;
import com.example.queues_management_application.GUI.SimulationFrame;
import com.example.queues_management_application.LogEvents;
import com.example.queues_management_application.Model.Server;
import com.example.queues_management_application.Model.Task;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int minTimeService;
    private int maxTimeService;
    private int minTimeArrival;
    private int maxTimeArrival;
    private int nrQueues;
    private int nrClients;
    public SelectionPolicy selectionPolicy;
    private ControllerQueueEvolution controllerQueueEvolution;
    private HelloController helloController;
    private List<Task> generatedTasks = new ArrayList<>();
    private Scheduler scheduler;
    private LogEvents logEvents = new LogEvents();

    public SimulationManager(SimulationFrame simulationFrame) {
        this.helloController = simulationFrame.getHelloController();
        this.controllerQueueEvolution = simulationFrame.getControllerQueueEvolution();
        this.timeLimit = helloController.getTimeMaxSim();
        this.minTimeService = helloController.getMinTS();
        this.maxTimeService = helloController.getMaxTS();
        this.minTimeArrival = helloController.getMinTA();
        this.maxTimeArrival = helloController.getMaxTA();
        this.nrQueues = helloController.getNumberQueues();
        this.nrClients = helloController.getNumberClients();
        this.selectionPolicy = SelectionPolicy.SHORTEST_TIME;
        this.scheduler = new Scheduler(nrQueues, this.controllerQueueEvolution, logEvents);
    }

    public void generateNRandomTasks() {
        Random random = new Random();
        for (int i = 0; i < nrClients; i++) {

            int arrivalTime = random.nextInt(maxTimeArrival - minTimeArrival + 1) + minTimeArrival;
            int serviceTime = random.nextInt(maxTimeService - minTimeService + 1) + minTimeService;

            Task newTask = new Task(i, arrivalTime, serviceTime);
            controllerQueueEvolution.addFigures(newTask);
            controllerQueueEvolution.addLabels(newTask);

            this.generatedTasks.add(newTask);

            //update interface's labels of tasks
            this.controllerQueueEvolution.updateLabels(newTask, arrivalTime, serviceTime, 0);

            Collections.sort(generatedTasks, Comparator.comparingInt(Task::getServiceTime));
        }
    }

    @Override
    public void run() {
        int currentTime = 0;
        generateNRandomTasks();
        while (!shouldStopSimulation(currentTime)) {

            //display the current time interface
            controllerQueueEvolution.setCounterLabel(currentTime);
            for (Task task : generatedTasks) {
                if (task.getArrivalTime() == currentTime) {
                    try {
                        scheduler.dispatchTask(task);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            //add the info in the text file
            try {
                logEvents.logEventWaiting(generatedTasks, currentTime);
                logEvents.logEventServers(scheduler.getServerList(), currentTime);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scheduler.computePeakHour(currentTime);
            currentTime = currentTime + 1;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        computeAverageServiceTime();
        controllerQueueEvolution.setPeakHour(scheduler.getPeakHour());
        stopSim();

        try {
            computeAverageWaitingTime();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean shouldStopSimulation(int currentTime) {
        boolean noMoreTasks = generatedTasks.stream().noneMatch(task -> task.getArrivalTime() >= currentTime);
        boolean allServersDone = true;
        for (Server server : scheduler.getServerList()) {
            if (!server.getTasksList().isEmpty())
                allServersDone = false;
        }
        boolean timeLimitExceeded = currentTime > timeLimit;
        return (noMoreTasks && allServersDone) || timeLimitExceeded;
    }

    private void stopSim() {
        for (Server server : this.scheduler.getServerList()) {
            server.setDone(false);
        }
    }

    public void computeAverageWaitingTime() throws IOException {
        float avg = 0;
        for (Task task : generatedTasks) {
            avg = avg + task.getWaitingTimeFinal();
        }
        avg = avg / nrClients;
        logEvents.avgWaitingTimeInFile(avg);
        controllerQueueEvolution.setAvgWaitingTime(avg);
    }

    public void computeAverageServiceTime() {
        float avg = 0;
        for (Task task : generatedTasks) {
            avg = avg + task.getServiceTime();
        }
        avg = avg / nrClients;
        controllerQueueEvolution.setAvgServiceTime(avg);
    }
}